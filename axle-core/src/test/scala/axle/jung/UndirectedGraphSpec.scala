package axle.jung

import org.scalatest._

import edu.uci.ics.jung.graph.UndirectedSparseGraph
import cats.implicits._
import spire.math.Real
import axle.algebra.UndirectedGraph
import axle.syntax.finite._
import axle.syntax.undirectedgraph._

class UndirectedGraphSpec extends FunSuite with Matchers {

  test("Undirected Graph") {

    class Edge(val weight: Real)

    val jug = UndirectedGraph.k2[UndirectedSparseGraph, String, Edge]

    val a = "a"
    val b = "b"
    val c = "c"
    val d = "d"

    val e1 = new Edge(1.1)

    val g = jug.make(
      List(a, b, c, d),
      List(
        (a, b, e1),
        (b, c, new Edge(2.2)),
        (c, d, new Edge(7.1)),
        (d, a, new Edge(-1.8)),
        (a, c, new Edge(8.0)),
        (b, d, new Edge(4.9))))

    g.vertexProjection.size should be(4)
    g.edgeProjection.size should be(6)
    g.connects(e1, a, b) should be(true)
    g.other(e1, a) should be(b)
    g.neighbors(a).size should be(3)
    g.edgesTouching(a).size should be(3)
    g.vertices.size should be(4)
    g.edges.size should be(6)
    g.findVertex(_ == a).get should be("a")
    g.filterEdges(_.weight > Real(0)).edges.size should be(5)
    g.degree(a) should be(3)
    g.areNeighbors(a, b) should be(true)
    g.isClique(List(a, b, c)) should be(true)
    g.forceClique(Set(a, b, c, d), (v1: String, v2: String) => new Edge(1d)).edges.size should be(6)
    vertexFunctorUDSG.map(g)(s => s + s).findVertex(_ == "aa").get should be("aa")
    edgeFunctorUDSG.map(g)(r => new Edge(r.weight + 1.1)).findEdge(a, b).weight should be(Real(2.2))
  }

}
