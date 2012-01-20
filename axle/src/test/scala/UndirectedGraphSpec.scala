package org.pingel.axle.graph

import org.specs2.mutable._
	  
class UndirectedGraphSpec extends Specification {

  import org.pingel.axle.graph._

  "Undirected Graph" should {
    "work" in {

      class UDE(v1: UDN, v2: UDN) extends UndirectedGraphEdge[UDN] {
	def getVertices() = (v1, v2)
      }
      
      class UDN(label: String) extends UndirectedGraphVertex[UDE] {
	def getLabel(): String = label
      }

      class UDG extends UndirectedGraph[UDN, UDE] { 
	def constructEdge(v1: UDN, v2: UDN) = new UDE(v1, v2)
      }

      val g = new UDG()
      val a = g.addVertex(new UDN("a"))
      val b = g.addVertex(new UDN("b"))
      val c = g.addVertex(new UDN("c"))

      g.size must be equalTo(3)
    }
  }

}
