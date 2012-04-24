package org.pingel.axle.graph

import scala.collection.JavaConversions._

object JungUndirectedGraphFactory extends JungUndirectedGraphFactory

trait JungUndirectedGraphFactory extends UndirectedGraphFactory {

  type G = JungUndirectedGraph[_, _]

  def graph[VP, EP](): JungUndirectedGraph[VP, EP] = new JungUndirectedGraph[VP, EP]() {}

  def graphFrom[OVP, OEP, NVP, NEP](other: UndirectedGraph[OVP, OEP])(convertVP: OVP => NVP, convertEP: OEP => NEP): JungUndirectedGraph[NVP, NEP] = {

    var jug: JungUndirectedGraph[NVP, NEP] = graph[NVP, NEP]()
    var vp2vp = Map[OVP, NVP]()

    other.getVertices().map(oldV => {
      val oldVP = oldV.getPayload()
      val newVP = convertVP(oldVP)
      jug.vertex(newVP)
      vp2vp += oldVP -> newVP
    })

    other.getEdges().map(oldE => {
      val (otherv1, otherv2) = oldE.getVertices()
      // TODO: lots of waste
      val newVP1 = convertVP(otherv1.getPayload)
      val newVP2 = convertVP(otherv2.getPayload)
      jug.edge(newVP1, newVP2, convertEP(oldE.getPayload), true)
    })

    jug
  }

  trait JungUndirectedGraph[VP, EP] extends UndirectedGraph[VP, EP] {

    import scala.collection._
    import edu.uci.ics.jung.graph.UndirectedSparseGraph

    type V = JungUndirectedGraphVertex

    type E = JungUndirectedGraphEdge

    type S = UndirectedSparseGraph[V, E]

    trait JungUndirectedGraphVertex extends UndirectedGraphVertex

    trait JungUndirectedGraphEdge extends UndirectedGraphEdge

    class JungUndirectedGraphVertexImpl(payload: VP, insert: Boolean) extends JungUndirectedGraphVertex {

      if (insert) {
        jungGraph.addVertex(payload)
      }

      def getPayload(): VP = payload
    }

    class JungUndirectedGraphEdgeImpl(v1: V, v2: V, payload: EP, insert: Boolean) extends JungUndirectedGraphEdge {

      if (insert) {
        jungGraph.addEdge(payload, v1.getPayload, v2.getPayload)
      }

      def getVertices(): (V, V) = (v1, v2)
      def getPayload(): EP = payload
    }

    var jungGraph = new UndirectedSparseGraph[VP, EP]()

    // TODO: make enVertex implicit
    def enVertex(payload: VP, insert: Boolean): JungUndirectedGraphVertex = vertex(payload, insert)

    def vertex(payload: VP, insert: Boolean = true): JungUndirectedGraphVertex = new JungUndirectedGraphVertexImpl(payload, insert)

    // TODO: make enEdge implicit
    def enEdge(payload: EP, insert: Boolean): JungUndirectedGraphEdge = {
      val endpoints = jungGraph.getEndpoints(payload)
      val v1 = endpoints.getFirst
      val v2 = endpoints.getSecond
      edge(enVertex(v1, false), enVertex(v2, false), payload, true)
    }

    def edge(v1: V, v2: V, payload: EP, insert: Boolean = true): JungUndirectedGraphEdge = new JungUndirectedGraphEdgeImpl(v1, v2, payload, insert)

    def edge(vp1: VP, vp2: VP, payload: EP, insert: Boolean = true): JungUndirectedGraphEdge = new JungUndirectedGraphEdgeImpl(enVertex(vp1, false), enVertex(vp2, false), payload, insert)

    def copyTo[VP, EP](other: UndirectedGraph[VP, EP]) = {
      // TODO
    }

    def unlink(e: E): Unit = jungGraph.removeEdge(e.getPayload)

    def unlink(v1: V, v2: V): Unit = getEdges(v1).filter(_.other(v1).equals(v2)).map(unlink(_))

    def areNeighbors(v1: V, v2: V) = getEdges(v1).exists(_.connects(v1, v2))

    def degree(v: V) = getEdges(v).size

    def getEdges(v: V): Set[E] = jungGraph.getIncidentEdges(v.getPayload).map(enEdge(_, false)).toSet

    def getNeighbors(v: V): Set[V] = jungGraph.getNeighbors(v.getPayload).map(enVertex(_, false)).toSet

    def delete(v: V): Unit = jungGraph.removeVertex(v.getPayload)

    // a "leaf" is vertex with only one neighbor
    def firstLeafOtherThan(r: V) = getVertices().find({ v => getNeighbors(v).size == 1 && !v.equals(r) })

    def eliminate(v: V, payload: (V, V) => EP) = {
      // "decompositions" page 3 (Definition 3, Section 9.3)
      // turn the neighbors of v into a clique
      val vs = getNeighbors(v)
      jungGraph.removeVertex(v.getPayload)
      forceClique(vs.asInstanceOf[Set[V]], payload)
    }

    // TODO there is probably a more efficient way to do this:
    def eliminate(vs: List[V], payload: (V, V) => EP): Unit = vs.map(eliminate(_, payload))

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
      val vv = new VisualizationViewer[VP, EP](layout) // interactive
      vv.setPreferredSize(new Dimension(width + border, height + border))

      val vertexPaint = new Transformer[VP, Paint]() {
        def transform(i: VP): Paint = Color.GREEN
      }

      val dash = List(10.0f).toArray

      val edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f)

      val edgeStrokeTransformer = new Transformer[EP, Stroke]() {
        def transform(edge: EP) = edgeStroke
      }

      val vertexLabelTransformer = new Transformer[VP, String]() {
        def transform(vertex: VP) = vertex.toString()
      }

      val edgeLabelTransformer = new Transformer[EP, String]() {
        def transform(edge: EP) = edge.toString()
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
