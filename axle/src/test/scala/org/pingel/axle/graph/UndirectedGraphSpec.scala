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
          def getLabel() = ""
        }

        class UDN(label: String) extends UndirectedGraphVertex {
          def getLabel(): String = label
        }

        def newEdge(v1: UDN, v2: UDN) = {
          val e = new UDE(v1, v2)
          addEdge(e)
          e
        }

        def newVertex(label: String) = {
          val v = new UDN(label)
          addVertex(v)
          v
        }
      }

      val g = new UDG()
      
      val a = g += "a"
      val b = g += "b"
      val c = g += "c"

      g += (a, b)
      g += (b, c)
      g += (c, a)
        
      g.size must be equalTo (3)
    }
  }

}
