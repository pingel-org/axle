package org.pingel.axle.graph

import scala.collection.JavaConversions._

object JungDirectedGraphFactory extends JungDirectedGraphFactory

trait JungDirectedGraphFactory extends DirectedGraphFactory {

  type G = JungDirectedGraph[_, _]

  def graph[VP, EP]() = new JungDirectedGraph[VP, EP]() {}

  def graphFrom[VP, EP, DG <: DirectedGraph[VP, EP]](other: DG): JungDirectedGraph[VP, EP] = {
    var jdg = graph[VP, EP]()
    other.getVertices().map( ov => jdg.vertex(ov.getPayload))
    other.getEdges().map( oe => jdg.edge(oe.getSource(), oe.getDest(), oe.getPayload()))
    jdg
  }

  trait JungDirectedGraph[VP, EP] extends DirectedGraph[VP, EP] {

    import scala.collection._
    import edu.uci.ics.jung.graph.DirectedSparseGraph

    type V = JungDirectedGraphVertex

    type E = JungDirectedGraphEdge

    type S = DirectedSparseGraph[VP, EP]

    trait JungDirectedGraphVertex extends DirectedGraphVertex

    trait JungDirectedGraphEdge extends DirectedGraphEdge

    class JungDirectedGraphVertexImpl(payload: VP) extends JungDirectedGraphVertex {

      // TODO: Would be nice to not have to create any new objects here

      createJungVertex
      // jungGraph.addVertex(v)

      def getPayload(): VP = payload
    }

    class JungDirectedGraphEdgeImpl(source: V, dest: V, payload: EP) extends JungDirectedGraphEdge {

      // TODO: Would be nice to not have to create any new objects here

      createJungEdge
      // jungGraph.addEdge(e)

      def getSource() = source
      def getDest() = dest
      def getPayload(): EP = payload
    }

    var jungGraph = new DirectedSparseGraph[VP, EP]()

    def getStorage() = jungGraph

    def size(): Int = jungGraph.getVertexCount()

    def getEdges(): Set[E] = jungGraph.getEdges.toSet

    def getVertices(): Set[V] = jungGraph.getVertices.toSet

    def getEdge(from: V, to: V): Option[E] = {
      val result = jungGraph.findEdge(from.getPayload(), to.getPayload())
      result match {
        case null => None
        case _ => Some(result)
      }
    }

    def edge(source: V, dest: V, payload: EP): JungDirectedGraphEdge = new JungDirectedGraphEdgeImpl(source, dest, payload)

    def vertex(payload: VP): JungDirectedGraphVertex = new JungDirectedGraphVertexImpl(payload)

    def removeAllEdgesAndVertices(): Unit = getVertices().map(jungGraph.removeVertex(_))

    def deleteEdge(e: E): Unit = jungGraph.removeEdge(e)

    def deleteVertex(v: V): Unit = jungGraph.removeVertex(v)

    def getLeaves(): Set[V] = getVertices().filter(isLeaf(_))

    def getNeighbors(v: V): Set[V] = {
      var result = Set[V]()
      for (neighbor <- jungGraph.getNeighbors(v.getPayload())) {
        result += neighbor
      }
      result
    }

    def precedes(v1: V, v2: V): Boolean = getPredecessors(v2).contains(v1)

    def getPredecessors(v: V): Set[V] = {
      var result = Set[V]()
      for (predecessor <- jungGraph.getPredecessors(v)) {
        result += predecessor
      }
      result
    }

    def isLeaf(v: V): Boolean = jungGraph.getSuccessorCount(v.getPayload()) == 0

    def getSuccessors(v: V): Set[V] = {
      var result = Set[V]()
      for (successor <- jungGraph.getSuccessors(v)) {
        result += successor
      }
      result
    }

    def outputEdgesOf(v: V): Set[E] = {
      var result = Set[E]()
      for (outEdge <- jungGraph.getOutEdges(v)) {
        result += outEdge
      }
      result
    }

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

    def removeInputs(vs: Set[V]): Unit = vs.map(v => {
      for (inEdge <- jungGraph.getInEdges(v)) {
        jungGraph.removeEdge(inEdge)
      }
    })

    def removeOutputs(vs: Set[V]): Unit = vs.map(v => {
      for (outEdge <- jungGraph.getOutEdges(v)) {
        jungGraph.removeEdge(outEdge)
      }
    })

    //TODO remove this method
    def removeSuccessor(v: V, successor: V): Unit = getEdge(v, successor).map(e => deleteEdge(e))

    //TODO remove this method
    def removePredecessor(v: V, predecessor: V): Unit = getEdge(predecessor, v).map(e => deleteEdge(e))

    // def moralGraph(): UndirectedGraph[_, _] = null // TODO !!!

    def isAcyclic() = true // TODO !!!

    def shortestPath(source: V, goal: V): Option[List[E]] = {
      import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath
      val dsp = new DijkstraShortestPath(jungGraph)
      val path = dsp.getPath(source.getPayload, goal.getPayload)
      path match {
        case null => None
        case _ => Some(path)
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
      val vv = new VisualizationViewer[VP, EP](layout) // interactive
      vv.setPreferredSize(new Dimension(width + border, height + border))

      val vertexPaint = new Transformer[VP, Paint]() {
        def transform(i: V): Paint = Color.GREEN
      }

      val dash = List(10.0f).toArray

      val edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f)

      val edgeStrokeTransformer = new Transformer[EP, Stroke]() {
        def transform(edge: E) = edgeStroke
      }

      val vertexLabelTransformer = new Transformer[VP, String]() {
        def transform(vertex: V) = vertex.getPayload().toString()
      }

      val edgeLabelTransformer = new Transformer[EP, String]() {
        def transform(edge: E) = edge.getPayload().toString()
      }

      vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint)
      vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer)
      vv.getRenderContext().setVertexLabelTransformer(vertexLabelTransformer) // new ToStringLabeller())
      vv.getRenderContext().setEdgeLabelTransformer(edgeLabelTransformer)
      vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR)

      //      val gm = new DefaultModalGraphMouse()
      //      gm.setMode(ModalGraphMouse.Mode.TRANSFORMING)
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
