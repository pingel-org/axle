package org.pingel.axle.graph {

  import scala.collection._

  trait DirectedGraph extends Graph {

    type V <: DirectedGraphVertex

    type E <: DirectedGraphEdge

    trait DirectedGraphVertex extends GraphVertex {
      // type E <: DirectedGraphEdge
    }

    trait DirectedGraphEdge extends GraphEdge {

      // type V <: DirectedGraphVertex

      def getSource(): V
      def getDest(): V
    }

    class DirectedGraphEdgeImpl(source: V, dest: V) extends DirectedGraphEdge {
      def getSource() = source
      def getDest() = dest
    }

    var vertex2outedges = Map[V, mutable.Set[E]]()
    var vertex2inedges = Map[V, mutable.Set[E]]()

    def getEdge(from: V, to: V): Option[E] = vertex2outedges(from).find(e => e.getDest == to)

    def addEdge(edge: E): E = {

      val source = edge.getSource()
      val dest = edge.getDest()

      edges += edge

      if (!vertex2outedges.contains(source)) {
        vertex2outedges += source -> mutable.Set[E]()
      }
      vertex2outedges(source) += edge

      if (!vertex2inedges.contains(dest)) {
        vertex2inedges += dest -> mutable.Set[E]()
      }
      vertex2inedges(dest) += edge

      edge
    }

    def addVertex(v: V) = {
      vertices += v
      v
    }

    def removeAllEdgesAndVertices(): Unit = {
      vertices = Set[V]()
      edges = Set[E]()
      vertex2outedges = Map[V, mutable.Set[E]]()
      vertex2inedges = Map[V, mutable.Set[E]]()
    }

    def deleteEdge(e: E): Unit = {
      edges -= e
      vertex2outedges.get(e.getSource()).map(_.remove(e))
      vertex2inedges.get(e.getDest()).map(_.remove(e))
    }

    def deleteVertex(v: V): Unit = {
      vertex2outedges.get(v).map(outEdges =>
        outEdges.map(e => {
          edges -= e
          vertex2inedges.get(e.getDest()).map(_.remove(e))
        }))
      vertex2outedges -= v

      vertex2inedges.get(v).map(inEdges =>
        inEdges.map(e => {
          edges -= e
          vertex2outedges.get(e.getSource()).map(_.remove(e))
        }))
      vertex2inedges -= v

      vertices -= v
    }

    def getLeaves(): Set[V] = getVertices().filter(isLeaf(_))

    def getNeighbors(v: V): Set[V] = {
      var result = Set[V]()
      vertex2outedges.get(v).map(_.map(edge => result += edge.getDest()))
      vertex2inedges.get(v).map(_.map(edge => result += edge.getSource()))
      result
    }

    def precedes(v1: V, v2: V): Boolean = getPredecessors(v2).contains(v1)

    def getPredecessors(v: V): Set[V] = {
      var result = Set[V]()
      vertex2inedges.get(v).map(_.map(edge => result += edge.getSource()))
      result
    }

    def isLeaf(v: V): Boolean = {
      val outEdges = vertex2outedges.get(v)
      outEdges == null || outEdges.size == 0
    }

    def getSuccessors(v: V): Set[V] = {
      var result = Set[V]()
      vertex2outedges.get(v).map(_.map(edge => result += edge.getDest()))
      result
    }

    def outputEdgesOf(v: V): Set[E] = {
      var result = Set[E]()
      vertex2outedges.get(v).map(outEdges => result ++= outEdges)
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
      vertex2inedges.get(v).map(incoming => {
        incoming.map(edge => edges -= edge)
        vertex2inedges += v -> null
      })
    })

    def removeOutputs(vs: Set[V]): Unit = vs.map(v => {
      vertex2outedges.get(v).map(outgoing => {
        outgoing.map(edge => edges -= edge)
        vertex2outedges += v -> null
      })
    })

    //TODO remove this method
    def removeSuccessor(v: V, successor: V): Unit = {
      vertex2outedges.get(v) map { outgoing =>
        outgoing.find(_.getDest().equals(successor)) map { edgeToRemove =>
          outgoing.remove(edgeToRemove)
          edges -= edgeToRemove
        }
      }
    }

    //TODO remove this method
    def removePredecessor(v: V, predecessor: V): Unit = {
      vertex2inedges.get(v) map { incoming =>
        incoming.find(_.getSource().equals(predecessor)).map(edgeToRemove => {
          incoming.remove(edgeToRemove)
          edges -= edgeToRemove // we should really only do this if it's the last of the pair of calls. ick.
        })
      }
    }

    // def moralGraph(): UndirectedGraph[_, _] = null // TODO !!!

    def isAcyclic() = true // TODO !!!

    // not so efficient:
    def _shortestPath(source: V, goal: V, visited: Set[V]): Option[List[E]] = (source == goal) match {
      case true => Some(List())
      case false => {
        val paths = getSuccessors(source)
          .filter(!visited.contains(_)).flatMap(newSuccessor => {
            getEdge(source, newSuccessor).flatMap(edge =>
              _shortestPath(newSuccessor, goal, visited + source).map(sp => edge :: sp)
            )
          }
          )
        paths.size match {
          case 0 => None
          case _ => Some(paths.reduceLeft(
            (l1, l2) => (l1.length < l2.length) match { case true => l1 case false => l2 }
          ))
        }
      }
    }

    def shortestPath(source: V, goal: V) = _shortestPath(source, goal, Set())

    def draw(): Unit = {
      val v = new DirectedGraphAsJUNG2(this)
      val jf = v.jframe
      jf.setVisible(true)
    }

  }

  class DirectedGraphAsJUNG2(ugf: DirectedGraph) // extends edu.uci.ics.jung.graph.UndirectedSparseGraph[ugf.type#V, ugf.type#E]
  {
    import edu.uci.ics.jung.graph.DirectedSparseGraph

    val ug = ugf

    var jungGraph = new DirectedSparseGraph[ug.type#V, ug.type#E]()
    ug.getVertices().map(jungGraph.addVertex(_))
    ug.getEdges().map(edge => jungGraph.addEdge(edge, edge.getSource(), edge.getDest()))

    import javax.swing.JFrame
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
    import edu.uci.ics.jung.graph.SparseMultigraph
    // import edu.uci.ics.jung.visualization.BasicVisualizationServer
    import edu.uci.ics.jung.visualization.VisualizationViewer
    // import edu.uci.ics.jung.visualization.control.CrossoverScalingControl
    // import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse
    import edu.uci.ics.jung.visualization.control.PluggableGraphMouse
    import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin
    import edu.uci.ics.jung.visualization.control.ModalGraphMouse
    // import edu.uci.ics.jung.visualization.control.ScalingGraphMousePlugin
    import edu.uci.ics.jung.visualization.control.TranslatingGraphMousePlugin
    import edu.uci.ics.jung.visualization.decorators.ToStringLabeller
    import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position

    import org.apache.commons.collections15.Transformer

    val width = 700
    val height = 700
    val border = 50

    def jframe(): JFrame = {

      // see
      // http://www.grotto-networking.com/JUNG/
      // http://www.grotto-networking.com/JUNG/JUNG2-Tutorial.pdf

      val layout = new FRLayout(jungGraph)
      layout.setSize(new Dimension(width, height))
      // val vv = new BasicVisualizationServer[ug.type#V, ug.type#E](layout) // non-interactive
      val vv = new VisualizationViewer[ug.type#V, ug.type#E](layout) // interactive
      vv.setPreferredSize(new Dimension(width + border, height + border))

      val vertexPaint = new Transformer[ug.type#V, Paint]() {
        def transform(i: ug.type#V): Paint = Color.GREEN
      }

      val dash = List(10.0f).toArray

      val edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f)

      val edgeStrokeTransformer = new Transformer[ug.type#E, Stroke]() {
        def transform(edge: ug.type#E) = edgeStroke
      }

      val vertexLabelTransformer = new Transformer[ug.type#V, String]() {
        def transform(vertex: ug.type#V) = "bar"
      }
      
      val edgeLabelTransformer = new Transformer[ug.type#E, String]() {
        def transform(edge: ug.type#E) = "foo"
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
