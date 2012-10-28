
package axle.graph

import org.specs2.mutable._

class DirectedGraphSpec extends Specification {

  "Directed Graph" should {
    "work" in {
      
      val (g0, vs) = JungDirectedGraph[String, String]() ++ List("a", "b", "c")
      val (g, es) = vs match {
        case a :: b :: c :: Nil => List((a, b, "hello"), (b, c, "world"), (c, a, "!"))
      }

      g.size must be equalTo (3)
    }
  }

  "REPL Demo" should {
    "work" in {

      val (g0, vs) = JungDirectedGraph[String, Double]() ++ List("a", "b", "c")
      val (g, es) = vs match {
        case a :: b :: c :: Nil => List((a, b, 0.3), (a, c, 0.2), (b, c, 0.4))
      }

      1 must be equalTo (1)
    }
  }

}
