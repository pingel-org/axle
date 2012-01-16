
package org.pingel.axle.graph {

  import scala.collection._

  trait DirectedGraphVertex[ET]

  trait DirectedGraphEdge[VT] {
    def getSource(): VT
    def getDest(): VT
  }

  trait DirectedGraph[VT, ET] {
    def isAcyclic(): Boolean
    def addVertex(v: VT): VT
    def addEdge(e: ET): ET
    def getVertices(): Set[VT]
    def getEdges(): Set[ET]
    def getNeighbors(v: VT): Set[VT]
    def collectAncestors(v: VT, result: mutable.Set[VT])
    def collectDescendants(v: VT, result: mutable.Set[VT])
    def getPredecessors(v: VT): Set[VT]
    def getSuccessors(v: VT): Set[VT]
    def removeInputs(ins: Set[VT])
    def removeOutputs(outs: Set[VT])
    def draw(): Unit
  }

  trait LabelledDirectedGraph[VT, ET] extends DirectedGraph[VT, ET]

  trait LabelledDirectedEdge[VT] extends DirectedGraphEdge[VT]

}
