
package org.pingel.axle.graph {

  import scala.collection._

  trait UndirectedGraph extends Graph {

    type V <: UndirectedGraphVertex
    type E <: UndirectedGraphEdge

    trait UndirectedGraphVertex extends GraphVertex

    trait UndirectedGraphEdge extends GraphEdge {

      def getVertices(): (V, V)

      def other(u: V): V = {
        val (v1, v2) = getVertices()
        u match {
          case _ if u.equals(v1) => v2
          case _ if u.equals(v2) => v1
          case _ => throw new Exception("can't find 'other' of a vertex that isn't on the edge itself")
        }
      }

      def connects(a1: V, a2: V) = {
        val (v1, v2) = getVertices()
        (v1 == a1 && v2 == a2) || (v2 == a1 && v1 == a2)
      }
    }

//    class UndirectedGraphEdgeImpl(v1: V, v2: V) extends UndirectedGraphEdge {
//      def getVertices() = (v1, v2)
//    }

    var vertex2edges = mutable.Map[V, mutable.Set[E]]()

    def addVertex(v: V): V = {
      vertices += v
      v
    }

    // dissertation idea: how best to have an "Edge" object without storing them

    def addEdge(e: E): E = {
      // assume that this edge isn't already in our list of edges
      edges += e
      val dble = e.getVertices()
      var es1 = getEdges(dble._1)
      es1.add(e)
      var es2 = getEdges(dble._2)
      es2.add(e)
      e
    }

    def copyTo(other: UndirectedGraph) = {
      // TODO
    }

    def unlink(e: E): Unit = {

      val dble = e.getVertices()

      var es1 = getEdges(dble._1)
      es1.remove(e)

      var es2 = getEdges(dble._2)
      es2.remove(e)

      edges -= e
    }

    def unlink(v1: V, v2: V): Unit = getEdges(v1).filter(_.other(v1).equals(v2)).map(unlink(_))

    def areNeighbors(v1: V, v2: V) = getEdges(v1).exists(_.connects(v1, v2))

    def isClique(vs: Set[V]): Boolean = {
      // vs.pairs().forall({ case (a, b) => ( (a == b) || areNeighbors(a, b) ) })
      var vList = mutable.ArrayBuffer[V]()
      vList ++= vs
      for (i <- 0 until vList.size) {
        for (j <- 0 until vList.size) {
          if (!areNeighbors(vList(i), vList(j))) {
            return false
          }
        }
      }
      true
    }

    def getNumEdgesToForceClique(vs: Set[V], payload: (V, V) => E#EP) = {

      var N = mutable.ArrayBuffer[V]()
      N ++= vs

      var result = 0

      for (i <- 0 to (N.size - 2)) {
        val vi = N(i)
        for (j <- (i + 1) until N.size) {
          val vj = N(j)
          if (!areNeighbors(vi, vj)) {
            addEdge(newEdge(vi, vj, payload(vi, vj)))
            result += 1
          }
        }
      }

      result
    }

    def forceClique(vs: Set[V], payload: (V, V) => E#EP) {

      var vList = mutable.ArrayBuffer[V]()
      vList ++= vs

      for (i <- 0 until (vList.size - 1)) {
        val vi = vList(i)
        for (j <- (i + 1) until vList.size) {
          val vj = vList(j)
          if (!areNeighbors(vi, vj)) {
            addEdge(newEdge(vi, vj, payload(vi, vj)))
          }
        }
      }

    }

    def vertexWithFewestEdgesToEliminateAmong(among: Set[V], payload: (V, V) => E#EP): Option[V] = {

      // assert: among is a subset of vertices

      var result: Option[V] = None
      var minSoFar = Integer.MAX_VALUE

      for (v <- among) {
        val x = getNumEdgesToForceClique(getNeighbors(v), payload)
        if (result == None) {
          result = Some(v)
          minSoFar = x
        } else if (x < minSoFar) {
          result = Some(v)
          minSoFar = x
        }
      }
      result
    }

    def vertexWithFewestNeighborsAmong(among: Set[V]): Option[V] = {
      // assert: among is a subset of vertices

      var result: Option[V] = None
      var minSoFar = Integer.MAX_VALUE

      for (v <- among) {
        val x = getNeighbors(v).size
        if (result == None) {
          result = Some(v)
          minSoFar = x
        } else if (x < minSoFar) {
          result = Some(v)
          minSoFar = x
        }
      }

      result
    }

    def degree(v: V) = getEdges(v).size

    def getEdges(v: V) = {
      if (!vertex2edges.contains(v)) {
        vertex2edges += v -> scala.collection.mutable.Set[E]()
      }
      vertex2edges(v)
    }

    def getNeighbors(v: V) = getEdges(v).map(_.other(v)).toSet

    def delete(v: V) = {
      val es = getEdges(v)
      vertices -= v
      vertex2edges.remove(v)
      for (e <- es) {
        edges -= e
        vertex2edges.get(e.other(v)) map { otherEdges => otherEdges.remove(e) }
      }
    }

    // a "leaf" is vertex with only one neighbor
    def firstLeafOtherThan(r: V) = vertices.find({
      v => getNeighbors(v).size == 1 && !v.equals(r)
    })

    def eliminate(v: V, payload: (V, V) => E#EP) = {
      // "decompositions" page 3 (Definition 3, Section 9.3)
      // turn the neighbors of v into a clique

      val es = getEdges(v)
      val vs = getNeighbors(v)

      vertices -= v
      vertex2edges.remove(v)
      for (e <- es) {
        edges -= e
      }

      forceClique(vs.asInstanceOf[Set[V]], payload)
    }

    // TODO there is probably a more efficient way to do this:
    def eliminate(vs: List[V], payload: (V, V) => E#EP): Unit = vs.map(eliminate(_, payload))

    def draw(): Unit = {
      val v = new UndirectedGraphAsJUNG2(this)
      val jf = v.jframe
      jf.setVisible(true)
    }

  }

  class UndirectedGraphAsJUNG2(ugf: UndirectedGraph) // extends edu.uci.ics.jung.graph.UndirectedSparseGraph[ugf.type#V, ugf.type#E]
  {
    import edu.uci.ics.jung.graph.UndirectedSparseGraph

    val ug = ugf

    var jungGraph = new UndirectedSparseGraph[ug.type#V, ug.type#E]()

    ug.getVertices().map(jungGraph.addVertex(_))

    ug.getEdges().map(edge => {
      val dbl = edge.getVertices()
      val v1 = dbl._1
      val v2 = dbl._2
      jungGraph.addEdge(edge, v1, v2)
    })

    import javax.swing.JFrame
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

    def jframe(): JFrame = {

      val layout = new FRLayout(jungGraph)
      layout.setSize(new Dimension(width, height))
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
        def transform(vertex: ug.type#V) = vertex.getPayload().toString()
      }

      val edgeLabelTransformer = new Transformer[ug.type#E, String]() {
        def transform(edge: ug.type#E) = edge.getPayload().toString()
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

  class SimpleGraph() extends UndirectedGraph {

    type V = SimpleVertex
    type E = SimpleEdge

    class SimpleVertex(vp: String) extends UndirectedGraphVertex {
      type VP = String
      def getPayload() = vp
    }

    def newVertex(name: String) = {
      val v = new SimpleVertex(name)
      addVertex(v)
      v
    }

    class SimpleEdge(v1: SimpleVertex, v2: SimpleVertex, ep: String) extends UndirectedGraphEdge {
      type EP = String
      def getVertices() = (v1, v2)
      def getPayload() = ep
    }

    def newEdge(v1: SimpleVertex, v2: SimpleVertex, ep: String) = {
      val result = new SimpleEdge(v1, v2, ep)
      addEdge(result)
      result
    }
  }

}
