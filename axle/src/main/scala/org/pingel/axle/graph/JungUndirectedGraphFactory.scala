package org.pingel.axle.graph

import scala.collection.JavaConversions._
import scala.collection._

object JungUndirectedGraphFactory extends JungUndirectedGraphFactory

trait JungUndirectedGraphFactory extends UndirectedGraphFactory {

  type G[VP, EP] = JungUndirectedGraph[VP, EP]

  def graph[A, B](): G[A, B] = new JungUndirectedGraph[A, B]() {}

  def graphFrom[OVP, OEP, NVP, NEP](other: UndirectedGraph[OVP, OEP])(convertVP: OVP => NVP, convertEP: OEP => NEP) = {

    val result = graph[NVP, NEP]()

    var ov2nv = Map[other.V, result.V]()

    other.getVertices().map(oldV => {
      val oldVP = oldV.getPayload()
      val newVP = convertVP(oldVP)
      val newV = result += newVP
      ov2nv += oldV -> newV
    })

    other.getEdges().map(oldE => {
      val (otherv1, otherv2) = oldE.getVertices()
      val nv1 = ov2nv(otherv1)
      val nv2 = ov2nv(otherv2)
      result += ((nv1, nv2), convertEP(oldE.getPayload))
    })

    result
  }

  trait JungUndirectedGraph[VP, EP] extends UndirectedGraph[VP, EP] {

    import scala.collection._
    import edu.uci.ics.jung.graph.UndirectedSparseGraph

    type V = JungUndirectedGraphVertex[VP]
    type E = JungUndirectedGraphEdge[EP]

    type S = UndirectedSparseGraph[V, E]

    trait JungUndirectedGraphVertex[P] extends UndirectedGraphVertex[P]

    trait JungUndirectedGraphEdge[P] extends UndirectedGraphEdge[P]

    class JungUndirectedGraphVertexImpl(payload: VP)
    extends JungUndirectedGraphVertex[VP] {

      val ok = jungGraph.addVertex(this)
      // TODO check 'ok'

      def getPayload(): VP = payload
    }

    class JungUndirectedGraphEdgeImpl(v1: V, v2: V, payload: EP, insert: Boolean)
    extends JungUndirectedGraphEdge[EP] {

      val ok = jungGraph.addEdge(this, v1, v2)
      // TODO check 'ok'

      def getVertices(): (V, V) = (v1, v2)

      def getPayload(): EP = payload
    }

    val jungGraph = new UndirectedSparseGraph[V, E]()

    def getStorage() = jungGraph

    def getVertices(): immutable.Set[V] = jungGraph.getVertices.toSet

    def getEdges(): immutable.Set[E] = jungGraph.getEdges.toSet

    def size(): Int = jungGraph.getVertexCount()

    def vertex(payload: VP): JungUndirectedGraphVertex[VP] = new JungUndirectedGraphVertexImpl(payload)

    def edge(v1: V, v2: V, payload: EP): JungUndirectedGraphEdge[EP] = new JungUndirectedGraphEdgeImpl(v1, v2, payload, true)

    def unlink(e: E): Unit = jungGraph.removeEdge(e)

    def unlink(v1: V, v2: V): Unit = getEdges(v1).filter(_.other(v1).equals(v2)).map(unlink(_))

    def areNeighbors(v1: V, v2: V) = getEdges(v1).exists(_.connects(v1, v2))

    def degree(v: V) = getEdges(v).size

    def getEdges(v: V): Set[E] = jungGraph.getIncidentEdges(v).toSet

    def getNeighbors(v: V): Set[V] = jungGraph.getNeighbors(v).toSet

    def delete(v: V): Unit = jungGraph.removeVertex(v)

    // a "leaf" is vertex with only one neighbor
    def firstLeafOtherThan(r: V) = getVertices().find({ v => getNeighbors(v).size == 1 && !v.equals(r) })

    def eliminate(v: V, payload: (V, V) => EP) = {
      // "decompositions" page 3 (Definition 3, Section 9.3)
      // turn the neighbors of v into a clique
      val vs = getNeighbors(v)
      jungGraph.removeVertex(v)
      forceClique(vs.asInstanceOf[Set[V]], payload)
    }

    // TODO there is probably a more efficient way to do this:
    def eliminate(vs: immutable.List[V], payload: (V, V) => EP): Unit = vs.map(eliminate(_, payload))

    def draw(): Unit = {
      val jf = jframe()
      jf.setVisible(true)
    }

    import javax.swing.JFrame

    def jframe(): JFrame = {

      import java.awt.Dimension
      import java.awt.BasicStroke
      import java.awt.Color
      import java.awt.Paint
      import java.awt.Stroke
      import java.awt.event.MouseEvent
      import edu.uci.ics.jung.algorithms.layout.FRLayout
      import edu.uci.ics.jung.algorithms.layout.Layout
      import edu.uci.ics.jung.graph.Graph
      import edu.uci.ics.jung.graph.SparseGraph
      import edu.uci.ics.jung.visualization.VisualizationViewer
      import edu.uci.ics.jung.visualization.control.PluggableGraphMouse
      import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin
      import edu.uci.ics.jung.visualization.control.TranslatingGraphMousePlugin
      import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position
      import org.apache.commons.collections15.Transformer

      val width = 700
      val height = 700
      val border = 50

      val layout = new FRLayout(jungGraph)
      layout.setSize(new Dimension(width, height))
      val vv = new VisualizationViewer[V, E](layout) // interactive
      vv.setPreferredSize(new Dimension(width + border, height + border))

      val vertexPaint = new Transformer[V, Paint]() {
        def transform(i: V): Paint = Color.GREEN
      }

      val dash = List(10.0f).toArray

      val edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f)

      val edgeStrokeTransformer = new Transformer[E, Stroke]() {
        def transform(edge: E) = edgeStroke
      }

      val vertexLabelTransformer = new Transformer[V, String]() {
        def transform(vertex: V) = vertex.toString()
      }

      val edgeLabelTransformer = new Transformer[E, String]() {
        def transform(edge: E) = edge.toString()
      }

      vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint)
      vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer)
      vv.getRenderContext().setVertexLabelTransformer(vertexLabelTransformer)
      vv.getRenderContext().setEdgeLabelTransformer(edgeLabelTransformer)
      vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR)

      val gm = new PluggableGraphMouse()
      gm.add(new TranslatingGraphMousePlugin(MouseEvent.BUTTON1))
      gm.add(new PickingGraphMousePlugin())
      vv.setGraphMouse(gm)

      val frame = new JFrame("graph name")
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
      frame.getContentPane().add(vv)
      frame.pack()
      frame
    }

  }

}
