package axle.visualize

import org.specs2.mutable.Specification

class GraphVisSpec extends Specification {

  "UndirectedGraph visualization" should {
    "render an SVG" in {

      import axle.jung._
      import axle.algebra.UndirectedGraph
      import edu.uci.ics.jung.graph.UndirectedSparseGraph

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

      val filename = "ug.svg"
      import axle.web._
      import axle._
      implicit val showEdge: Show[Edge] = new Show[Edge] { def text(e: Edge): String = "" }
      svg(g, filename)

      new java.io.File(filename).exists must be equalTo true
    }
  }

  "DirectedGraph visualization" should {
    "render an SVG" in {

      import axle.jung._
      import axle.algebra.DirectedGraph
      import edu.uci.ics.jung.graph.DirectedSparseGraph

      class Edge

      val jdg = DirectedGraph.k2[DirectedSparseGraph, String, Edge]

      val a = "a"
      val b = "b"
      val c = "c"
      val d = "d"

      val dg = jdg.make(List(a, b, c, d),
        List(
          (a, b, new Edge),
          (b, c, new Edge),
          (c, d, new Edge),
          (d, a, new Edge),
          (a, c, new Edge),
          (b, d, new Edge)))

      val filename = "dg.svg"
      import axle.web._
      import axle._
      implicit val showEdge: Show[Edge] = new Show[Edge] { def text(e: Edge): String = "" }
      svg(dg, filename)

      new java.io.File(filename).exists must be equalTo true
    }
  }
}