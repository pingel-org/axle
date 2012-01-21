
package org.pingel.axle.graph {

  import scala.collection._

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

  trait UndirectedGraphVertex[UE <: UndirectedGraphEdge[_]] extends GraphVertex[UE] {

    def getLabel(): String

  }

  trait UndirectedGraphEdge[UV <: UndirectedGraphVertex[_]] extends GraphEdge[UV] {

    def getVertices(): (UV, UV)

    def other(u: UV): UV = {
      val (v1, v2) = getVertices()
      u match {
        case _ if u.equals(v1) => v2
        case _ if u.equals(v2) => v1
        case _ => throw new Exception("can't find 'other' of a vertex that isn't on the edge itself")
      }
    }

    def connects(a1: UV, a2: UV) = {
      val (v1, v2) = getVertices()
      (v1 == a1 && v2 == a2) || (v2 == a1 && v1 == a2)
    }
  }

  class UndirectedGraphEdgeImpl[UV <: UndirectedGraphVertex[_]](v1: UV, v2: UV)
    extends UndirectedGraphEdge[UV] {
    def getVertices() = (v1, v2)
  }

  trait UndirectedGraph[UV <: UndirectedGraphVertex[UE], UE <: UndirectedGraphEdge[UV]]
    extends Graph[UV, UE] {

    var vertex2edges = mutable.Map[UV, mutable.Set[UE]]()

    def addVertex(v: UV): UV = {
      vertices += v
      v
    }

    // dissertation idea: how best to have an "Edge" object without storing them

    def addEdge(e: UE): UE = {

      // assume that this edge isn't already in our list of edges

      edges += e
      val dble = e.getVertices()

      var es1 = getEdges(dble._1)
      es1.add(e)

      var es2 = getEdges(dble._2)
      es2.add(e)

      e
    }

    def copyTo(other: UndirectedGraph[UV, UE]) = {
      // TODO
    }

    def constructEdge(v1: UV, v2: UV): UE

    def unlink(e: UE): Unit = {

      val dble = e.getVertices()

      var es1 = getEdges(dble._1)
      es1.remove(e)

      var es2 = getEdges(dble._2)
      es2.remove(e)

      edges -= e
    }

    def unlink(v1: UV, v2: UV): Unit = {
      // TODO optimize

      val edges = getEdges(v1)
      for (edge <- edges) {
        if (edge.other(v1).equals(v2)) {
          unlink(edge)
        }
      }
    }

    def areNeighbors(v1: UV, v2: UV): Boolean = {
      val es = getEdges(v1)
      for (e <- es) {
        if (e.connects(v1, v2)) {
          return true
        }
      }
      false
    }

    def isClique(vs: Set[UV]): Boolean = {

      var vList = mutable.ArrayBuffer[UV]()
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

    def getNumEdgesToForceClique(vs: Set[UV]) = {

      var N = mutable.ArrayBuffer[UV]()
      N ++= vs

      var result = 0

      for (i <- 0 to (N.size - 2)) {
        val vi = N(i)
        for (j <- (i + 1) until N.size) {
          val vj = N(j)
          if (!areNeighbors(vi, vj)) {
            addEdge(constructEdge(vi, vj))
            result += 1
          }
        }
      }

      result
    }

    def forceClique(vs: Set[UV]) {

      var vList = mutable.ArrayBuffer[UV]()
      vList ++= vs

      for (i <- 0 to (vList.size - 2)) {
        val vi = vList(i)
        for (j <- (i + 1) until vList.size) {
          val vj = vList(j)
          if (!areNeighbors(vi, vj)) {
            addEdge(constructEdge(vi, vj))
          }
        }
      }

    }

    def vertexWithFewestEdgesToEliminateAmong(among: Set[UV]): Option[UV] = {

      // assert: among is a subset of vertices

      var result: Option[UV] = None
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

    def vertexWithFewestNeighborsAmong(among: Set[UV]): Option[UV] = {
      // assert: among is a subset of vertices

      var result: Option[UV] = None
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

    def degree(v: UV) = getEdges(v).size

    def getEdges(v: UV) = {
      if (!vertex2edges.contains(v)) {
        vertex2edges += v -> scala.collection.mutable.Set[UE]()
      }
      vertex2edges(v)
    }

    def getNeighbors(v: UV) = getEdges(v).map({ _.other(v) }).toSet

    def delete(v: UV) = {
      val es = getEdges(v)
      vertices -= v
      vertex2edges.remove(v)
      for (e <- es) {
        edges -= e
        vertex2edges.get(e.other(v)) map { otherEdges => otherEdges.remove(e) }
      }
    }

    // a "leaf" is vertex with only one neighbor
    def firstLeafOtherThan(r: UV) = vertices.find({
      v => getNeighbors(v).size == 1 && !v.equals(r)
    })

    def eliminate(v: UV) = {
      // "decompositions" page 3 (Definition 3, Section 9.3)
      // turn the neighbors of v into a clique

      val es = getEdges(v)
      val vs = getNeighbors(v)

      vertices -= v
      vertex2edges.remove(v)
      for (e <- es) {
        edges -= e
      }

      forceClique(vs.asInstanceOf[Set[UV]])
    }

    def eliminate(vs: List[UV]): Unit = {
      // TODO there is probably a more efficient way to do this
      for (v <- vs) {
        eliminate(v)
      }
    }

    class UndirectedVertexStringer(jung2pingel: Map[Vertex, UV]) extends VertexStringer {

      // def getLabel(v: Vertex) = jung2pingel(v).getLabel()

      def getLabel(v: ArchetypeVertex) = "TODO" // jung2pingel(v.getEquivalentVertex(this)).getLabel()

    }

    def draw() = {

      var jungGraph = new UndirectedSparseGraph()

      var pingel2jung = Map[UV, Vertex]()
      var jung2pingel = Map[Vertex, UV]()

      for (pv <- getVertices()) {
        val vertex = new SimpleUndirectedSparseVertex()
        jungGraph.addVertex(vertex)
        pingel2jung += pv -> vertex
        jung2pingel += vertex -> pv
      }

      for (edge <- getEdges()) {
        val dbl = edge.getVertices()
        val v1 = dbl._1
        val v2 = dbl._2
        val jedge = new UndirectedSparseEdge(pingel2jung(v1), pingel2jung(v2))
        jungGraph.addEdge(jedge)
      }

      var pr = new PluggableRenderer()
      //      pr.setVertexPaintFunction(new ModelVertexPaintFunction(m));
      //      pr.setEdgeStrokeFunction(new ModelEdgeStrokeFunction(m));
      //      pr.setEdgeShapeFunction(new EdgeShape.Line());
      pr.setVertexStringer(new UndirectedVertexStringer(jung2pingel))

      val layout = new FRLayout(jungGraph)

      var jf = new JFrame()
      var gd = new GraphDraw(jungGraph)
      gd.getVisualizationViewer().setGraphLayout(layout)
      gd.getVisualizationViewer().setRenderer(pr)
      jf.getContentPane().add(gd)
      jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
      jf.pack()
      jf.setVisible(true)
    }

  }

}
