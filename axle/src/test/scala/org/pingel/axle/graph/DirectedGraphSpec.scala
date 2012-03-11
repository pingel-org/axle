
package org.pingel.axle.graph

import org.specs2.mutable._

class DirectedGraphSpec extends Specification {

  import org.pingel.axle.graph._

  "Directed Graph" should {
    "work" in {

      class DG extends DirectedGraph {

        type E = DE
        
        type V = DN
        
        class DE(v1: DN, v2: DN) extends DirectedGraphEdge {
          def getVertices() = (v1, v2)
          def getSource() = v1
          def getDest() = v2
        }

        class DN(label: String) extends DirectedGraphVertex {
          def getLabel(): String = label
        }

        def newEdge(source: DN, dest: DN) = new DE(source, dest)

        def newVertex(label: String) = new DN(label)
      }

      val g = new DG()
      val a = g += "a"
      val b = g += "b"
      val c = g += "c"

      g += a -> b
      g += b -> c
      g += c -> a
        
      g.size must be equalTo (3)
    }
  }

}
