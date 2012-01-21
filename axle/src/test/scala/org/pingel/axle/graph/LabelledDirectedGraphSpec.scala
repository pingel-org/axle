package org.pingel.axle.graph

import org.specs2.mutable._

class LabelledDirectedGraphSpec extends Specification {

  import org.pingel.axle.graph._

  "Labelled Directed Graph" should {
    "work" in {

      var ldg = new LabelledDirectedGraphImpl()
      //val a = ldg.addVertex(new DirectedGraphVertexImpl())
      //val b = ldg.addVertex(new LDE("b"))
      //val c = ldg.addVertex(new LDE("c"))
      ldg.size must be equalTo(0)
    }

  }

}