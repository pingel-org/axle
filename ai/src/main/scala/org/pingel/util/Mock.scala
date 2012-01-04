
package org.pingel.util {

	case class UndirectedGraphVertex[ET]()

	case class UndirectedGraphEdge[VT](v1: Any, v2: Any)

	case class UndirectedGraph[VT, ET]() {
		def addVertex(v: VT)
		def addEdge(e: ET)
		def getVertices(): Set[VT]
		def getNeighbors(v: VT): Set[VT]
		def getEdges(): Set[ET]
	}

	case class DirectedGraphVertex[ET]()

	case class DirectedGraphEdge[VT](source: VT, dest: VT) {
		def getSource(): VT
		def getDest(): VT
	}

	case class DirectedGraph[VT, ET]() {
		def addVertex(v: VT)
		def addEdge(e: ET)
		def getVertices(): Set[VT]
		def getEdges(): Set[ET]
		def getNeighbors(v: VT): Set[VT]
		def getPredecessors(v: VT): Set[VT]
		def getSuccessors(v: VT): Set[VT]
		def removeInputs(ins: Set[VT])
		def removeOutputs(outs: Set[VT])
	}

	case class Matrix[T]() {
	  def numRows: Int
	  def numCols: Int
	}
	case class Lister()
	case class ListCrossProduct[T](ts: Seq[Seq[T]])
	case class CrossProduct[T]()
	case class PowerSet()
	case class Collector()

}

/*
package edu.uci.ics.jung {

  package graph {
    case class DirectedGraph
    case class Edge
    case class Vertex
    package impl {
      case class DirectedSparseEdge
      case class DirectedSparseGraph
      case class DirectedSparseVertex
    }
    package decorators {
      case class EdgeShape
      case class EdgeStrokeFunction
      case class VertexPaintFunction
      case class VertexStringer
    }
  }

  package decorators {
    case class EdgeShape
    case class EdgeStrokeFunction
    case class VertexPaintFunction
    case class VertexStringer
  }

  package visualization {
    case class FRLayout
    case class GraphDraw
    case class Layout
    case class PluggableRenderer
  }
  
}
*/
