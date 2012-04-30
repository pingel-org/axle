package org.pingel.axle.graph

import org.specs2.mutable._

class UndirectedGraphSpec extends Specification {

  import org.pingel.axle.graph.JungUndirectedGraphFactory._

  "Undirected Graph" should {
    "work" in {

      val g = graph[String, String]()

      val a = g += "a"
      val b = g += "b"
      val c = g += "c"

      g += ((a, b), "hello")
      g += ((b, c), "world")
      g += ((c, a), "!")

      g.size must be equalTo (3)
    }
  }

  "REPL Demo" should {
    "work" in {

      val g = graph[String, Double]()

      1 must be equalTo (1)
    }
  }

}
