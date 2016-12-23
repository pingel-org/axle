
package axle.jung

import org.scalatest._

import edu.uci.ics.jung.graph.DirectedSparseGraph
import axle.algebra.DirectedGraph
import spire.math.Real
import spire.implicits._
import axle.syntax.finite._
import axle.syntax.directedgraph._
import cats.implicits._

class DirectedGraphSpec extends FunSuite with Matchers {

  test("Directed Graph") {

    class Edge(val weight: Real)

    val jdg = DirectedGraph.k2[DirectedSparseGraph, String, Edge]

    val a = "a"
    val b = "b"
    val c = "c"
    val d = "d"

    val abEdge = new Edge(1.1)
    val acEdge = new Edge(-1.1)

    val g = jdg.make(List(a, b, c, d),
      List(
        (a, b, abEdge),
        (b, c, new Edge(4.1)),
        (c, d, new Edge(5.1)),
        (d, a, new Edge(8.1)),
        (a, c, acEdge),
        (b, d, new Edge(5.3))))

    g.size should be(4)
    g.neighbors(a).size should be(3)
    g.edgesTouching(a).size should be(3)
    g.degree(a) should be(3)
    g.other(abEdge, a) should be(b)
    g.other(abEdge, b) should be(a)
    g.connects(abEdge, a, b) should be(true)
    g.precedes(a, b) should be(true)
    g.predecessors(b) should be(Set(a))
    g.successors(a) should be(Set(b, c))
    g.outputEdgesOf(a) should be(Set(abEdge, acEdge))
    g.descendantsIntersectsSet(a, Set(d)) should be(true)
    // TODO g.follows(a, b)
    g.vertices.size should be(4)
    g.edges.size should be(6)
    g.findVertex(_ == "a").get should be("a")
    g.filterEdges(_.weight > Real(0d)).edges.size should be(5)
    g.removeInputs(Set(a)).edges.size should be(5)
    g.removeOutputs(Set(d)).edges.size should be(5)
    g.areNeighbors(a, b) should be(true)
    g.isClique(List(a, b, c)) should be(true)
    g.forceClique(Set(a, b, c, d), (v1: String, v2: String) => new Edge(1d)).edges.size should be(12)
    g.leaves should be(Set.empty)
    vertexFunctorDSG.map(g)(s => s + s).findVertex(_ == "aa").get should be("aa")
    edgeFunctorDSG.map(g)(r => new Edge(r.weight + 1.1)).findEdge("a", "b").weight should be(Real(2.2))

  }

}
