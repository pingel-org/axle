package axle.visualize

import org.scalatest._
import cats.implicits._

class GraphVisSpec extends FunSuite with Matchers {

  test("UndirectedGraph visualization renders an SVG") {

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

    import cats.Show
    implicit val showEdge: Show[Edge] = new Show[Edge] { def show(e: Edge): String = "" }

    import axle.web._
    val svgName = "ug.svg"
    svg(g, svgName)

    import axle.awt._
    val pngName = "ug.png"
    png(g, pngName)

    new java.io.File(svgName).exists should be(true)
    new java.io.File(pngName).exists should be(true)
  }

  test("DirectedGraph visualization renders an SVG") {

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

    import cats.Show
    implicit val showEdge: Show[Edge] = new Show[Edge] { def show(e: Edge): String = "" }

    // import axle._
    import axle.web._
    val svgName = "dg.svg"
    svg(dg, svgName)

    import axle.awt._
    val pngName = "dg.png"
    png(dg, pngName)

    new java.io.File(svgName).exists should be(true)
    new java.io.File(pngName).exists should be(true)
  }

}
