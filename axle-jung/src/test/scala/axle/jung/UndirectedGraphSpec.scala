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
      g.neighbors(a).size must be equalTo 3
      // g.edges.size must be equalTo 6
      g.vertices.size must be equalTo 4
      g.findVertex(_ == "a").get must be equalTo "a"
      vertexFunctorUDSG.map(g)(s => s + s).findVertex(_ == "aa").get must be equalTo "aa"
    }
  }

  "REPL Demo" should {
    "work" in {

      class Edge(weight: Real)

      val jug = UndirectedGraph.k2[UndirectedSparseGraph, String, Edge]

      val g = jug.make(
        List("a"),
        Nil)

      g.size must be equalTo 1
    }
  }

}
