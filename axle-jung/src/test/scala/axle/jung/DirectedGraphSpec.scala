
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

      val g = jdg.make[String, String](List("a", "b", "c", "d"),
        (vs: Seq[String]) => vs match {
          case a :: b :: c :: d :: Nil => List((a, b, ""), (b, c, ""), (c, d, ""), (d, a, ""), (a, c, ""), (b, d, ""))
          case _                       => Nil
        })

      g.size must be equalTo 4
    }
  }

  "REPL Demo" should {
    "work" in {

      val g = jdg.make[String, Double](List("a", "b", "c", "d"),
        (vs: Seq[String]) => vs match {
          case a :: b :: c :: d :: Nil => List((a, b, 0.3), (a, c, 0.2), (b, c, 0.4), (c, d, 2.4))
          case _                       => Nil
        })

      1 must be equalTo (1)
    }
  }

}
