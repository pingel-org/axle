package axle.graph

import org.specs2.mutable._

class UndirectedGraphSpec extends Specification {

  "Undirected Graph" should {
    "work" in {

      val g = JungUndirectedGraph(
        vps = List("a", "b", "c"),
        ef = (vs: Seq[JungUndirectedGraphVertex[String]]) => vs match {
          case a :: b :: c :: Nil => List((a, b, "hello"), (b, c, "world"), (c, a, "!"))
        })

      g.size must be equalTo (3)
    }
  }

  "REPL Demo" should {
    "work" in {

      val g = JungUndirectedGraph[String, Double]()

      1 must be equalTo (1)
    }
  }

}
