
package axle.graph

import org.specs2.mutable._

class DirectedGraphSpec extends Specification {

  import JungDirectedGraphFactory._

  "Directed Graph" should {
    "work" in {

      val g = graph[String, String]()

      val a = g += "a"
      val b = g += "b"
      val c = g += "c"

      val ab = g += (a -> b, "hello")
      val bc = g += (b -> c, "world")
      val ca = g += (c -> a, "!")

      g.size must be equalTo (3)
    }
  }

  "REPL Demo" should {
    "work" in {

      val g = graph[String, Double]()

      val a = g += "a"
      val b = g += "b"
      val c = g += "c"

      val ab = g += ((a -> b), 0.3)
      val ac = g += ((a -> c), 0.2)
      val bc = g += ((b -> c), 0.4)

      1 must be equalTo (1)
    }
  }

}
