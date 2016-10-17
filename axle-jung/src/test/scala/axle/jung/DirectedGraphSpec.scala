
package axle.jung

import org.specs2.mutable.Specification

import edu.uci.ics.jung.graph.DirectedSparseGraph
import axle.algebra.DirectedGraph
import spire.math.Real
import spire.implicits._
import axle.syntax.finite._
import axle.syntax.directedgraph._

class DirectedGraphSpec extends Specification {

  "Directed Graph" should {
    "work" in {

      class Edge(val weight: Real)

      val jdg = DirectedGraph.k2[DirectedSparseGraph, String, Edge]

      val a = "a"
      val b = "b"
      val c = "c"
      val d = "d"

      val e1 = new Edge(1.1)

      val g = jdg.make(List(a, b, c, d),
        List(
          (a, b, e1),
          (b, c, new Edge(4.1)),
          (c, d, new Edge(5.1)),
          (d, a, new Edge(8.1)),
          (a, c, new Edge(-1.1)),
          (b, d, new Edge(5.3))))

      g.size must be equalTo 4
      g.neighbors(a).size must be equalTo 3
      g.edgesTouching(a).size must be equalTo 3
      g.degree(a) must be equalTo 3
      g.other(e1, a) must be equalTo b
      g.connects(e1, a, b) must be equalTo true
      g.predecessors(b) must be equalTo Set(a)
      g.vertices.size must be equalTo 4
      g.edges.size must be equalTo 6
      g.findVertex(_ == "a").get must be equalTo "a"
      g.filterEdges(_.weight > Real(0d)).edges.size must be equalTo 5
      g.areNeighbors(a, b) must be equalTo true
      g.isClique(List(a, b, c)) must be equalTo true
      g.leaves must be equalTo Set.empty
      vertexFunctorDSG.map(g)(s => s + s).findVertex(_ == "aa").get must be equalTo "aa"
      edgeFunctorDSG.map(g)(r => new Edge(r.weight + 1.1)).findEdge("a","b").weight must be equalTo Real(2.2)
    }
  }

}
