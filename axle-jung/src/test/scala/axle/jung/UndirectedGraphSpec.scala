package axle.jung

import axle.algebra.UndirectedGraph
import org.specs2.mutable._
import spire.math._
import edu.uci.ics.jung.graph.UndirectedSparseGraph

class UndirectedGraphSpec extends Specification {

  val jug = UndirectedGraph[UndirectedSparseGraph]

  "Undirected Graph" should {
    "work" in {

      class Edge

      val g = jug.make[String, Edge](List("a", "b", "c", "d"),
        (vs: Seq[String]) => vs match {
          case a :: b :: c :: d :: Nil => List(
            (a, b, new Edge),
            (b, c, new Edge),
            (c, d, new Edge),
            (d, a, new Edge),
            (a, c, new Edge),
            (b, d, new Edge))
          case _ => Nil
        })

      jug.size(g) must be equalTo (4)
    }
  }

  "REPL Demo" should {
    "work" in {

      class Edge(weight: Real)

      val g = jug.make[String, Real](
        List("a"),
        (vs: Seq[String]) => Nil)

      1 must be equalTo (1)
    }
  }

}
