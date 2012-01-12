
package org.pingel.util {

  import scala.collection._
  import java.lang.StringBuffer
  
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
		// def vertexWithFewestNeighborsAmong
	}

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
	
	object Matrix {
	  
	  def zeros[T](numRows: Int, numCols: Int): Matrix[T] = {
	    var result = new DenseMatrix[T](numRows, numCols)
	    // TODO
	    result
	  }
	  
	}
	
	trait Matrix[T] {
	  def numRows: Int
	  def numCols: Int
	  def valueAt(r: Int, c: Int): T
	  def setValueAt(r: Int, c: Int, v: T): Unit = { } // TODO
	}
	
	class DenseMatrix[T](nr: Int, nc: Int) extends Matrix[T] {
	  def numRows = nr
	  def numCols = nc
	  // TODO: initialize
	}
	
	case class Lister()
	case class ListCrossProduct[T](ts: Seq[Seq[T]])
	case class CrossProduct[T]()
	case class PowerSet()
	case class Collector()
	
	trait Printable {
	  def print(s: String=""): Unit
	  def println(s: String=""): Unit
	  def indent(i: Int): Unit
	}
	
	class PrintableStringBuffer(sb: StringBuffer) extends Printable {
	  def print(s: String=""): Unit = {
	    sb.append(s)
	  }
	  def println(s: String=""): Unit = {
	    sb.append(s+"\n")
	  }
	  def indent(i: Int): Unit = {
	    (0 until i).map( x => sb.append("   "))
	  }
	}
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
