package org.pingel.axle.graph

import org.specs2.mutable._

class UndirectedGraphSpec extends Specification {

  import org.pingel.axle.graph._

  "Undirected Graph" should {
    "work" in {

      class UDG extends UndirectedGraph {

    	type E = UDE
    	
    	type V = UDN
        
        class UDE(v1: UDN, v2: UDN) extends UndirectedGraphEdge {
          def getVertices() = (v1, v2)
        }

        class UDN(label: String) extends UndirectedGraphVertex {
          def getLabel(): String = label
        }

        def newEdge(v1: UDN, v2: UDN) = new UDE(v1, v2)

        def newVertex(label: String) = new UDN(label)
      }

      val g = new UDG()
      val a = g.addVertex(g.newVertex("a"))
      val b = g.addVertex(g.newVertex("b"))
      val c = g.addVertex(g.newVertex("c"))

      g.size must be equalTo (3)
    }
  }

}
