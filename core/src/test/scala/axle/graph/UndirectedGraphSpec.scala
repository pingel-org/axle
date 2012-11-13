package axle.graph

import org.specs2.mutable._

class UndirectedGraphSpec extends Specification {

  "Undirected Graph" should {
    "work" in {

      val g = JungUndirectedGraph(
        vps = List("a", "b", "c", "d"),
        ef = (vs: Seq[JungUndirectedGraphVertex[String]]) => vs match {
          case a :: b :: c :: d :: Nil => List((a, b, ""), (b, c, ""), (c, d, ""), (d, a, ""), (a, c, ""), (b, d, ""))
        })

      g.size must be equalTo (3)
    }
  }

  "REPL Demo" should {
    "work" in {

      val g = JungUndirectedGraph[String, Double](List("a"), (vs: Seq[JungUndirectedGraphVertex[String]]) => Nil)

      1 must be equalTo (1)
    }
  }

}
