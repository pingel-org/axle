
package org.pingel.axle.graph {

  import scala.collection._

  trait UndirectedGraph extends Graph {

    type V <: UndirectedGraphVertex
    
    type E <: UndirectedGraphEdge

    trait UndirectedGraphVertex extends GraphVertex {
      def getLabel(): String
    }

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

    class UndirectedGraphEdgeImpl(v1: V, v2: V)
      extends UndirectedGraphEdge {
      def getVertices() = (v1, v2)
    }

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

    def getNumEdgesToForceClique(vs: Set[V]) = {

      var N = mutable.ArrayBuffer[V]()
      N ++= vs

      var result = 0

      for (i <- 0 to (N.size - 2)) {
        val vi = N(i)
        for (j <- (i + 1) until N.size) {
          val vj = N(j)
          if (!areNeighbors(vi, vj)) {
            addEdge(newEdge(vi, vj))
            result += 1
          }
        }
      }

      result
    }

    def forceClique(vs: Set[V]) {

      var vList = mutable.ArrayBuffer[V]()
      vList ++= vs

      for (i <- 0 until (vList.size - 1)) {
        val vi = vList(i)
        for (j <- (i + 1) until vList.size) {
          val vj = vList(j)
          if (!areNeighbors(vi, vj)) {
            addEdge(newEdge(vi, vj))
          }
        }
      }

    }

    def vertexWithFewestEdgesToEliminateAmong(among: Set[V]): Option[V] = {

      // assert: among is a subset of vertices

      var result: Option[V] = None
      var minSoFar = Integer.MAX_VALUE

      for (v <- among) {
        val x = getNumEdgesToForceClique(getNeighbors(v))
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

    def eliminate(v: V) = {
      // "decompositions" page 3 (Definition 3, Section 9.3)
      // turn the neighbors of v into a clique

      val es = getEdges(v)
      val vs = getNeighbors(v)

      vertices -= v
      vertex2edges.remove(v)
      for (e <- es) {
        edges -= e
      }

      forceClique(vs.asInstanceOf[Set[V]])
    }

    // TODO there is probably a more efficient way to do this:
    def eliminate(vs: List[V]): Unit = vs.map(eliminate(_))

    def draw(): Unit = {
      val v = new UndirectedGraphVisualization(this)
      val jf = v.jframe
      jf.setVisible(true)
    }

  }

  class UndirectedGraphVisualization(ug: UndirectedGraph) {

    import javax.swing.JFrame
    import edu.uci.ics.jung.graph.Vertex
    import edu.uci.ics.jung.graph.ArchetypeVertex
    import edu.uci.ics.jung.graph.decorators.VertexStringer
    import edu.uci.ics.jung.graph.impl.SimpleUndirectedSparseVertex
    import edu.uci.ics.jung.graph.impl.UndirectedSparseEdge
    import edu.uci.ics.jung.graph.impl.UndirectedSparseGraph
    import edu.uci.ics.jung.visualization.FRLayout
    import edu.uci.ics.jung.visualization.GraphDraw
    import edu.uci.ics.jung.visualization.Layout
    import edu.uci.ics.jung.visualization.PluggableRenderer

    class UndirectedVertexStringer(jung2pingel: Map[Vertex, ug.type#V]) extends VertexStringer {
      // def getLabel(v: Vertex) = jung2pingel(v).getLabel()
      def getLabel(v: ArchetypeVertex) = "TODO" // jung2pingel(v.getEquivalentVertex(this)).getLabel()
    }

    def jframe(): JFrame = {

      var jungGraph = new UndirectedSparseGraph()

      var axle2jung = Map[ug.type#V, Vertex]()
      var jung2axle = Map[Vertex, ug.type#V]()

      for (pv <- ug.getVertices()) {
        val vertex = new SimpleUndirectedSparseVertex()
        jungGraph.addVertex(vertex)
        axle2jung += pv -> vertex
        jung2axle += vertex -> pv
      }

      for (edge <- ug.getEdges()) {
        val dbl = edge.getVertices()
        val v1 = dbl._1
        val v2 = dbl._2
        val jedge = new UndirectedSparseEdge(axle2jung(v1), axle2jung(v2))
        jungGraph.addEdge(jedge)
      }

      var pr = new PluggableRenderer()
      // pr.setVertexPaintFunction(new ModelVertexPaintFunction(m));
      // pr.setEdgeStrokeFunction(new ModelEdgeStrokeFunction(m));
      // pr.setEdgeShapeFunction(new EdgeShape.Line());
      pr.setVertexStringer(new UndirectedVertexStringer(jung2axle))

      val layout = new FRLayout(jungGraph)

      var jf = new JFrame()
      var gd = new GraphDraw(jungGraph)
      gd.getVisualizationViewer().setGraphLayout(layout)
      gd.getVisualizationViewer().setRenderer(pr)
      jf.getContentPane().add(gd)
      jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
      jf.pack()
      jf
    }
  }

  class SimpleGraph() extends UndirectedGraph {

    type V = SimpleVertex
    type E = SimpleEdge
    
    class SimpleVertex(label: String) extends UndirectedGraphVertex {
      def getLabel() = label
    }

    def newVertex(name: String) = new SimpleVertex(name)
    
    class SimpleEdge(v1: SimpleVertex, v2: SimpleVertex) extends UndirectedGraphEdge {
      def getVertices() = (v1, v2)
    }

    def newEdge(v1: SimpleVertex, v2: SimpleVertex) = {
      val result = new SimpleEdge(v1, v2)
      addEdge(result)
      result
    }
  }

}
