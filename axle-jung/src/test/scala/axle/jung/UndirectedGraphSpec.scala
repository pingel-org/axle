package axle.jung

import axle.algebra.UndirectedGraph
import org.specs2.mutable._
import spire.math._
import edu.uci.ics.jung.graph.UndirectedSparseGraph
import axle.syntax.finite._

class UndirectedGraphSpec extends Specification {

  "Undirected Graph" should {
    "work" in {

      class Edge

      val jug = UndirectedGraph.k2[UndirectedSparseGraph, String, Edge]

      val a = "a"
      val b = "b"
      val c = "c"
      val d = "d"

      val g = jug.make(List(a, b, c, d),
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

      class Edge(weight: Real)

      val jug = UndirectedGraph.k2[UndirectedSparseGraph, String, Edge]

      val g = jug.make(
        List("a"),
        Nil)

      1 must be equalTo (1)
    }
  }

}
