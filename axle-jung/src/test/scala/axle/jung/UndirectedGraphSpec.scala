package axle.jung

import axle.algebra.UndirectedGraph
import axle.algebra.Vertex
import org.specs2.mutable._
import spire.math._

class UndirectedGraphSpec extends Specification {

  val jug = UndirectedGraph[JungUndirectedGraph]

  "Undirected Graph" should {
    "work" in {

      val g = jug.make(List("a", "b", "c", "d"),
        (vs: Seq[Vertex[String]]) => vs match {
          case a :: b :: c :: d :: Nil => List((a, b, ""), (b, c, ""), (c, d, ""), (d, a, ""), (a, c, ""), (b, d, ""))
          case _                       => Nil
        })

      jug.size(g) must be equalTo (4)
    }
  }

  "REPL Demo" should {
    "work" in {

      val g = jug.make[String, Real](
        List("a"),
        (vs: Seq[Vertex[String]]) => Nil)

      1 must be equalTo (1)
    }
  }

}
