
package axle.jung

import axle.algebra.DirectedGraph
import axle.algebra.Vertex
import axle.algebra.Edge
import org.specs2.mutable._
import spire.math._
import spire.implicits._

class DirectedGraphSpec extends Specification {

  val jdg = implicitly[DirectedGraph[JungDirectedGraph]]

  "Directed Graph" should {
    "work" in {

      val g = jdg.make(List("a", "b", "c", "d"),
        (vs: Seq[Vertex[String]]) => vs match {
          case a :: b :: c :: d :: Nil => List((a, b, ""), (b, c, ""), (c, d, ""), (d, a, ""), (a, c, ""), (b, d, ""))
          case _                       => Nil
        })

      jdg.size(g) must be equalTo (4)
    }
  }

  "REPL Demo" should {
    "work" in {

      val g = jdg.make(List("a", "b", "c", "d"),
        (vs: Seq[Vertex[String]]) => vs match {
          case a :: b :: c :: d :: Nil => List((a, b, 0.3), (a, c, 0.2), (b, c, 0.4), (c, d, 2.4))
          case _                       => Nil
        })

      1 must be equalTo (1)
    }
  }

}
