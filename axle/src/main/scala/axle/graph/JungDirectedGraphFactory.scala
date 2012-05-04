package axle.graph

import scala.collection.JavaConversions._
import scala.collection._

object JungDirectedGraphFactory extends JungDirectedGraphFactory

trait JungDirectedGraphFactory extends DirectedGraphFactory {

  type G[VP, EP] = JungDirectedGraph[VP, EP]

  def graph[A, B](): G[A, B] = new JungDirectedGraph[A, B]() {}

  def graphFrom[OVP, OEP, NVP, NEP](other: DirectedGraph[OVP, OEP])(
    convertVP: OVP => NVP, convertEP: OEP => NEP) = {

    val result = graph[NVP, NEP]()

    var ov2nv = Map[other.V, result.V]()

    other.getVertices().map(ov => {
      val nv = result += convertVP(ov.getPayload)
      ov2nv += ov -> nv
    })

    other.getEdges().map(oe => {
      val nSource = ov2nv(oe.getSource)
      val nDest = ov2nv(oe.getDest)
      result += (nSource -> nDest, convertEP(oe.getPayload))
    })

    result
  }

  trait JungDirectedGraph[VP, EP] extends DirectedGraph[VP, EP] {

    import scala.collection._
    import edu.uci.ics.jung.graph.DirectedSparseGraph

    type V = JungDirectedGraphVertex[VP]
    type E = JungDirectedGraphEdge[EP]

    type S = DirectedSparseGraph[V, E]

    trait JungDirectedGraphVertex[P] extends DirectedGraphVertex[P]

    trait JungDirectedGraphEdge[P] extends DirectedGraphEdge[P]

    class JungDirectedGraphVertexImpl(var payload: VP) extends JungDirectedGraphVertex[VP] {

      val ok = jungGraph.addVertex(this)
      // TODO check 'ok'

      def getPayload(): VP = payload
      
      def setPayload(p: VP) = payload = p
    }

    class JungDirectedGraphEdgeImpl(source: V, dest: V, var payload: EP) extends JungDirectedGraphEdge[EP] {

      val ok = jungGraph.addEdge(this, source, dest)
      // TODO check 'ok'

      def getSource() = source
      def getDest() = dest
      def getPayload(): EP = payload
      def setPayload(p: EP) = payload = p
   }

    val jungGraph = new DirectedSparseGraph[V, E]()

    // TODO: make enVertex implicit

    //    def enEdge(payload: EP): JungDirectedGraphEdge[EP] = {
    //      val endpoints = jungGraph.getEndpoints(payload)
    //      edge(endpoints.getFirst, endpoints.getSecond, payload, false)
    //    }

    def getStorage() = jungGraph

    def size(): Int = jungGraph.getVertexCount()

    def getEdges(): immutable.Set[E] = jungGraph.getEdges.toSet

    def getVertices(): immutable.Set[V] = jungGraph.getVertices.toSet

    def getEdge(from: V, to: V): Option[E] = {
      val result = jungGraph.findEdge(from, to)
      result match {
        case null => None
        case _ => Some(result)
      }
    }

    def edge(source: V, dest: V, payload: EP): E = new JungDirectedGraphEdgeImpl(source, dest, payload)

    def vertex(payload: VP): V = new JungDirectedGraphVertexImpl(payload)

    def removeAllEdgesAndVertices(): Unit = getVertices().map(jungGraph.removeVertex(_))

    def deleteEdge(e: E): Unit = jungGraph.removeEdge(e)

    def deleteVertex(v: V): Unit = jungGraph.removeVertex(v)

    def getLeaves(): Set[V] = getVertices().filter(isLeaf(_))

    def getNeighbors(v: V): Set[V] = jungGraph.getNeighbors(v).toSet

    def precedes(v1: V, v2: V): Boolean = getPredecessors(v2).contains(v1)

    def getPredecessors(v: V): Set[V] = jungGraph.getPredecessors(v).toSet

    def isLeaf(v: V): Boolean = jungGraph.getSuccessorCount(v) == 0

    def getSuccessors(v: V): Set[V] = jungGraph.getSuccessors(v).toSet

    def outputEdgesOf(v: V): Set[E] = jungGraph.getOutEdges(v).toSet

    def descendantsIntersectsSet(v: V, s: Set[V]): Boolean =
      s.contains(v) || s.exists(x => descendantsIntersectsSet(x, s))

    def collectDescendants(v: V, result: mutable.Set[V]): Unit = {
      // inefficient
      if (!result.contains(v)) {
        result.add(v)
        getSuccessors(v).map(collectDescendants(_, result))
      }
    }

    def collectAncestors(v: V, result: mutable.Set[V]): Unit = {
      // inefficient
      if (!result.contains(v)) {
        result.add(v)
        getPredecessors(v).map(collectAncestors(_, result))
      }
    }

    def collectAncestors(vs: Set[V], result: mutable.Set[V]): Unit = vs.map(collectAncestors(_, result))

    def removeInputs(vs: Set[V]): Unit =
      vs.map(v => jungGraph.getInEdges(v).map(inEdge => jungGraph.removeEdge(inEdge)))

    def removeOutputs(vs: Set[V]): Unit =
      vs.map(v => jungGraph.getOutEdges(v).map(outEdge => jungGraph.removeEdge(outEdge)))

    //TODO remove this method
    def removeSuccessor(v: V, successor: V): Unit = getEdge(v, successor).map(e => deleteEdge(e))

    //TODO remove this method
    def removePredecessor(v: V, predecessor: V): Unit = getEdge(predecessor, v).map(e => deleteEdge(e))

    // def moralGraph(): UndirectedGraph[_, _] = null // TODO !!!

    def isAcyclic() = true // TODO !!!

    def shortestPath(source: V, goal: V): Option[immutable.List[E]] = {
      if (source == goal) {
        Some(Nil)
      } else {
        import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath
        val dsp = new DijkstraShortestPath(jungGraph)
        val path = dsp.getPath(source, goal)
        path match {
          case null => None
          case _ => path.length match {
            case 0 => None
            case _ => Some(path.toList)
          }
        }
      }
    }

    def draw(): Unit = {
      // val v = new DirectedXGraphAsJUNG2(this)
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

      // import edu.uci.ics.jung.algorithms.layout.CircleLayout
      import edu.uci.ics.jung.algorithms.layout.FRLayout
      import edu.uci.ics.jung.algorithms.layout.Layout
      import edu.uci.ics.jung.graph.Graph
      import edu.uci.ics.jung.graph.SparseGraph
      // import edu.uci.ics.jung.graph.SparseMultigraph
      // import edu.uci.ics.jung.visualization.BasicVisualizationServer
      import edu.uci.ics.jung.visualization.VisualizationViewer
      // import edu.uci.ics.jung.visualization.control.CrossoverScalingControl
      // import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse
      import edu.uci.ics.jung.visualization.control.PluggableGraphMouse
      import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin
      // import edu.uci.ics.jung.visualization.control.ModalGraphMouse
      // import edu.uci.ics.jung.visualization.control.ScalingGraphMousePlugin
      import edu.uci.ics.jung.visualization.control.TranslatingGraphMousePlugin
      // import edu.uci.ics.jung.visualization.decorators.ToStringLabeller
      import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position

      import org.apache.commons.collections15.Transformer

      val width = 700
      val height = 700
      val border = 50

      // see
      // http://www.grotto-networking.com/JUNG/
      // http://www.grotto-networking.com/JUNG/JUNG2-Tutorial.pdf

      val layout = new FRLayout(jungGraph)
      layout.setSize(new Dimension(width, height))
      // val vv = new BasicVisualizationServer[ug.type#V, ug.type#E](layout) // non-interactive
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
        def transform(vertex: V) = vertex.getPayload.toString
      }

      val edgeLabelTransformer = new Transformer[E, String]() {
        def transform(edge: E) = edge.getPayload.toString
      }

      vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint)
      vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer)
      vv.getRenderContext().setVertexLabelTransformer(vertexLabelTransformer) // new ToStringLabeller())
      vv.getRenderContext().setEdgeLabelTransformer(edgeLabelTransformer)
      vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR)

      // val gm = new DefaultModalGraphMouse()
      // gm.setMode(ModalGraphMouse.Mode.TRANSFORMING)
      val gm = new PluggableGraphMouse()
      gm.add(new TranslatingGraphMousePlugin(MouseEvent.BUTTON1))
      gm.add(new PickingGraphMousePlugin())
      // gm.add(new ScalingGraphMousePlugin(new CrossoverScalingControl(), 0, 1.1f, 0.9f))
      vv.setGraphMouse(gm)

      val frame = new JFrame("graph name")
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
      frame.getContentPane().add(vv)
      frame.pack()
      frame
    }

  }

}
