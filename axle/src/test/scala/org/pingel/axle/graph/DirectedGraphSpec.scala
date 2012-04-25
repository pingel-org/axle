
package org.pingel.axle.graph

import org.specs2.mutable._

class DirectedGraphSpec extends Specification {

  import org.pingel.axle.graph.JungDirectedGraphFactory

  "Directed Graph" should {
    "work" in {

      var g = JungDirectedGraphFactory.graph[String, String]()
      val a = g += "a"
      val b = g += "b"
      val c = g += "c"

      g += (a -> b, "")
      g += (b -> c, "")
      g += (c -> a, "")
        
      g.size must be equalTo (3)
    }
  }

}
