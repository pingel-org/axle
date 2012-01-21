
package org.pingel.axle.graph {

  // trait LabelledDirectedGraph[VT, ET] extends DirectedGraph[VT, ET]

  trait LabelledDirectedEdge[DV <: DirectedGraphVertex[_]]
  extends DirectedGraphEdge[DV] {
    def getLabel(): String
  }

  class LabelledDirectedEdgeImpl[DV <: DirectedGraphVertex[_]](
    source: DV, label: String, dest: DV)
  extends LabelledDirectedEdge[DV] {
    def getSource = source
    def getDest = dest
    def getLabel = label
  }

  //trait DirectedGraph[DV <: DirectedGraphVertex[DE], DE <: DirectedGraphEdge[DV]] 
  //extends Graph[DV, DE]
  
  trait LabelledDirectedGraph[DV <: DirectedGraphVertex[LDE], LDE <: LabelledDirectedEdge[DV]]
  extends DirectedGraph[DV, LDE] {
  }

  class LabelledDirectedGraphImpl[DV <: DirectedGraphVertex[LDE], LDE <: LabelledDirectedEdge[DV]] 
  extends LabelledDirectedGraph[DV, LDE]
}
