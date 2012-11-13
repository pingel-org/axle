
package axle.graph

import org.specs2.mutable._

class DirectedGraphSpec extends Specification {

  "Directed Graph" should {
    "work" in {

      val g = JungDirectedGraph(List("a", "b", "c", "d"),
        (vs: Seq[JungDirectedGraphVertex[String]]) => vs match {
          case a :: b :: c :: d :: Nil => List((a, b, ""), (b, c, ""), (c, d, ""), (d, a, ""), (a, c, ""), (b, d, ""))
        }
      )

      g.size must be equalTo (4)
    }
  }

  "REPL Demo" should {
    "work" in {

      val g = JungDirectedGraph(List("a", "b", "c", "d"),
        (vs: Seq[JungDirectedGraphVertex[String]]) => vs match {
          case a :: b :: c :: d :: Nil => List((a, b, 0.3), (a, c, 0.2), (b, c, 0.4), (c, d, 2.4))
        })

      1 must be equalTo (1)
    }
  }

}
