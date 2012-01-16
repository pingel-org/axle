
package org.pingel.axle.graph {

  // trait LabelledDirectedGraph[VT, ET] extends DirectedGraph[VT, ET]

  trait LabelledDirectedEdge[V]
  extends DirectedGraphEdge[V] {
    def getLabel(): String
  }

  class LabelledDirectedEdgeImpl[V](source: V, label: String, dest: V)
  extends LabelledDirectedEdge[V] {
	def getSource = source
	def getDest = dest
    def getLabel = label
  }

  
  trait LabelledDirectedGraph[V <: DirectedGraphVertex[E], E <: LabelledDirectedEdge[V]]
  extends DirectedGraph[V, E] {
  }

  class LabelledDirectedGraphImpl[V <: DirectedGraphVertex[E], E <: LabelledDirectedEdge[V]] 
  extends LabelledDirectedGraph[V, E]
  
  object LabelledDirectedGraphTest {
    def main(args: Array[String]) {
      var ldg = new LabelledDirectedGraphImpl()
    }
  }

}
