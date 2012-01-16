
package org.pingel.axle.graph {

  import scala.collection._

  trait UndirectedGraphVertex[ET]

  trait UndirectedGraphEdge[VT] {
    def getVertices(): (VT, VT)
  }

  trait UndirectedGraph[VT, ET] {
    def addVertex(v: VT): VT
    def addEdge(e: ET): ET
    def getVertices(): Set[VT]
    def getNeighbors(v: VT): Set[VT]
    def getEdges(): Set[ET]
    def draw(): Unit
    def eliminate(v: VT): Unit
    // def vertexWithFewestNeighborsAmong
  }

}
