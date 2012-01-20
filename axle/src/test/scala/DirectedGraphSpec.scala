
package org.pingel.axle.graph

import org.specs2.mutable._
	  
class DirectedGraphSpec extends Specification {

  import org.pingel.axle.graph._

  "Directed Graph" should {
    "work" in {

      class DE(v1: DN, v2: DN) extends DirectedGraphEdge[DN]
      {
	def getVertices() = (v1, v2)
	def getSource() = v1
	def getDest() = v2
      }
      
      class DN(label: String) extends DirectedGraphVertex[DE]
      {
	def getLabel(): String = label
      }

      class DG extends DirectedGraph[DN, DE] { }

      val g = new DG()
      val a = g.addVertex(new DN("a"))
      val b = g.addVertex(new DN("b"))
      val c = g.addVertex(new DN("c"))

      g.size must be equalTo(3)
    }
  }

}
