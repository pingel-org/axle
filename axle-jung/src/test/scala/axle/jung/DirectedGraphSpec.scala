
package axle.jung

import org.specs2.mutable.Specification

import edu.uci.ics.jung.graph.DirectedSparseGraph
import axle.algebra.DirectedGraph
import axle.syntax.directedgraph.directedGraphOps
import spire.implicits.StringOrder

class DirectedGraphSpec extends Specification {

  val jdg = DirectedGraph[DirectedSparseGraph]

  "Directed Graph" should {
    "work" in {

      class Edge

      val g = jdg.make[String, Edge](List("a", "b", "c", "d"),
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

      g.size must be equalTo 4
    }
  }

  "REPL Demo" should {
    "work" in {

      class Edge(weight: Double)

      val g = jdg.make[String, Edge](List("a", "b", "c", "d"),
        (vs: Seq[String]) => vs match {
          case a :: b :: c :: d :: Nil => List(
            (a, b, new Edge(0.3)),
            (a, c, new Edge(0.2)),
            (b, c, new Edge(0.4)),
            (c, d, new Edge(2.4)))
          case _ => Nil
        })

      1 must be equalTo (1)
    }
  }

}
