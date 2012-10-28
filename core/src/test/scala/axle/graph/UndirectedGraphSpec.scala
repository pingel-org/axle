package axle.graph

import org.specs2.mutable._

class UndirectedGraphSpec extends Specification {

  "Undirected Graph" should {
    "work" in {

      val (g0, vs) = JungUndirectedGraph[String, String]() ++ List("a", "b", "c")
      val (g, es) = vs match {
        case a :: b :: c :: Nil => g0 ++ List((a, b, "hello"), (b, c, "world"), (c, a, "!"))
      }

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
