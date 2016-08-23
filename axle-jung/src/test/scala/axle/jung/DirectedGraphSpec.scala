
package axle.jung

import org.specs2.mutable.Specification

import edu.uci.ics.jung.graph.DirectedSparseGraph
import axle.algebra.DirectedGraph
import axle.syntax.finite._

class DirectedGraphSpec extends Specification {

  "Directed Graph" should {
    "work" in {

      class Edge

      val jdg = DirectedGraph.k2[DirectedSparseGraph, String, Edge]

      val a = "a"
      val b = "b"
      val c = "c"
      val d = "d"

      val g = jdg.make(List(a, b, c, d),
        List(
          (a, b, new Edge),
          (b, c, new Edge),
          (c, d, new Edge),
          (d, a, new Edge),
          (a, c, new Edge),
          (b, d, new Edge)))

      g.size must be equalTo 4
    }
  }

  "REPL Demo" should {
    "work" in {

      class Edge(weight: Double)

      val jdg = DirectedGraph.k2[DirectedSparseGraph, String, Edge]

      val a = "a"
      val b = "b"
      val c = "c"
      val d = "d"

      val g = jdg.make(List(a, b, c, d),
        List(
          (a, b, new Edge(0.3)),
          (a, c, new Edge(0.2)),
          (b, c, new Edge(0.4)),
          (c, d, new Edge(2.4))))

      1 must be equalTo (1)
    }
  }

}
