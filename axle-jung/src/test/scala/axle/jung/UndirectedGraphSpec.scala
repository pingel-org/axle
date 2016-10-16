package axle.jung

import axle.algebra.UndirectedGraph
import org.specs2.mutable.Specification
import spire.math.Real
import edu.uci.ics.jung.graph.UndirectedSparseGraph
import spire.implicits._
import axle.syntax.finite._
import axle.syntax.undirectedgraph._

class UndirectedGraphSpec extends Specification {

  "Undirected Graph" should {
    "work" in {

      class Edge(val weight: Real)

      val jug = UndirectedGraph.k2[UndirectedSparseGraph, String, Edge]

      val a = "a"
      val b = "b"
      val c = "c"
      val d = "d"

      val g = jug.make(List(a, b, c, d),
        List(
          (a, b, new Edge(1.1)),
          (b, c, new Edge(2.2)),
          (c, d, new Edge(7.1)),
          (d, a, new Edge(-1.8)),
          (a, c, new Edge(8.0)),
          (b, d, new Edge(4.9))))

      g.size must be equalTo 4
      g.neighbors(a).size must be equalTo 3
      g.edgesTouching(a).size must be equalTo 3
      g.vertices.size must be equalTo 4
      g.edges.size must be equalTo 6
      g.findVertex(_ == a).get must be equalTo "a"
      g.filterEdges(_.weight > Real(4.1)).size must be equalTo 3
      g.degree(a) must be equalTo 3
      g.areNeighbors(a, b) must be equalTo true
      g.isClique(List(a, b, c)) must be equalTo false
      vertexFunctorUDSG.map(g)(s => s + s).findVertex(_ == "aa").get must be equalTo "aa"
      edgeFunctorUDSG.map(g)(r => new Edge(r.weight + 1.1)).findEdge(a, b).weight must be equalTo Real(2.2)
    }
  }

}
