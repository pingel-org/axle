/**
 * Copyright (c) 2008 Adam Pingel
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package org.pingel.util

import javax.swing.JFrame

import edu.uci.ics.jung.graph.Vertex
import edu.uci.ics.jung.graph.decorators.VertexStringer
import edu.uci.ics.jung.graph.impl.SimpleUndirectedSparseVertex
import edu.uci.ics.jung.graph.impl.UndirectedSparseEdge
import edu.uci.ics.jung.graph.impl.UndirectedSparseGraph
import edu.uci.ics.jung.visualization.FRLayout
import edu.uci.ics.jung.visualization.GraphDraw
import edu.uci.ics.jung.visualization.Layout
import edu.uci.ics.jung.visualization.PluggableRenderer

abstract class UndirectedGraph[V <: UndirectedGraphVertex[E], E <: UndirectedGraphEdge[V]] {

	var vertices = Set[V]()
	var edges = Set[E]()
	var vertex2edges = Map[V, Set[E]]()

	def addVertex(v: V) {
		vertices.add(v)
	}

	def getVertices() = vertices

    def getNumVertices() = vertices.size

	def getEdges() = edges

	// dissertation idea: how best to have an "Edge" object without storing them
	
	def addEdge(e: E) {
		
		// assume that this edge isn't already in our list of edges
		
		edges.add(e)
		val dble = e.getVertices()
		
		var es1 = getEdges(dble.getFirst())
		es1.add(e)
		
		var es2 = getEdges(dble.getSecond())
		es2.add(e)
	}

	def constructEdge(v1: V, v2: V): E

    def unlink(e: E) = {
	  
        val dble = e.getVertices()
        
        var es1 = getEdges(dble._1)
        es1.remove(e)

        var es2 = getEdges(dble._2)
        es2.remove(e)
        
        edges.remove(e)
    }
    
	def unlink(v1: V, v2: V) = {
	    // TODO optimize
	    
	    val edges = getEdges(v1)
	    for( edge <- edges ) {
	        if( edge.other(v1).equals(v2) ) {
	            unlink(edge)
	        }
	    }
    }
    
	def areNeighbors(v1: V, v2: V): Boolean = {

		val es = getEdges(v1)
		for(e <- es) {
			if( e.connects(v1, v2) ) {
				return true
			}
		}
		false
	}
	
	def isClique(vs: Set[V]): Boolean = {
	  
		var vList = new ArrayList[V]()
		vList.addAll(vs)
		for( i <- 0 to (vList.size - 1) ) {
			for( j <- 0 to (vList.size - 1) ) {
				if( ! areNeighbors(vList(i), vList(j)) ) {
					return false
				}
			}
		}
		true
	}

	def getNumEdgesToForceClique(vs: Set[V]) = {
	  
		var N = new ArrayList<V>()
		N.addAll(vs)

		var result = 0
		
		for( i <- 0 to (N.size - 2) ) {
			val vi = N.get(i);
			for( j <- (i+1) to (N.size - 1)) {
				val vj = N(j)
				if( ! areNeighbors(vi, vj) ) {
					addEdge(constructEdge(vi, vj))
					result += 1
				}
			}
		}

		result
	}
	
	def forceClique(vs: Set[V]) {
		
		var vList = new ArrayList[V]()
		vList.addAll(vs)
		
		for( i <- 0 to (vList.size - 2) ) {
			val vi = vList(i)
			for( j <- (i+1) to (vList.size - 1) ) {
				val vj = vList(j)
				if( ! areNeighbors(vi, vj) ) {
					addEdge(constructEdge(vi, vj))
				}
			}
		}
		
	}

	def vertexWithFewestEdgesToEliminateAmong(among: Set[V]) = {
	  
		// assert: among is a subset of vertices
		
		var result: V = null
		var minSoFar = Integer.MAX_VALUE
		
		for( v <- among ) {
		  val x = getNumEdgesToForceClique(getNeighbors(v))
		  if( result == null ) {
			  result = v
			  minSoFar = x
		  }
		  else if( x < minSoFar ) {
			  result = v
			  minSoFar = x
		  }
		}
		result
	}

	def vertexWithFewestNeighborsAmong(among: Set[V]) = {
		// assert: among is a subset of vertices
		
		var result: V = null
		var minSoFar = Integer.MAX_VALUE
		
		for( v <- among ) {
			val x = getNeighbors(v).size
			if( result == null ) {
				result = v
				minSoFar = x
			}
			else if( x < minSoFar ) {
				result = v
				minSoFar = x
			}
		}
		
		result
	}
	
	def degree(v: V) = getEdges(v).size

	def getEdges(v: V) = {
		var result = Set[E]()
		result = vertex2edges(v)
		if( result == null ) {
			result = Set[E]()
			vertex2edges += v -> result
		}
		result
	}

	def getNeighbors(v: V) = {
		var result = Set[V]()
		for(e <- getEdges(v)) {
			result.add(e.other(v))
		}
		result
	}

	def copyTo(other: UndirectedGraph[V, E]): Unit
	
	def delete(v: V) = {
		val es = getEdges(v)
		vertices.remove(v)
		vertex2edges.remove(v)
		for( e <- es ) {
			edges.remove(e)
			vertex2edges.get(e.other(v)).remove(e)
		}
	}

	def firstLeafOtherThan(r: V): V = {
		// a "leaf" is vertex with only one neighbor
		for( v <- vertices ) {
			if( getNeighbors(v).size == 1 && ! v.equals(r) ) {
				return v
			}
		}
		null
	}
	
	def eliminate(v: V) = {
		// "decompositions" page 3 (Definition 3, Section 9.3)
		// turn the neighbors of v into a clique
		
		val es = getEdges(v)
		val vs = getNeighbors(v)

		vertices.remove(v)
		vertex2edges.remove(v)
		for( e <- es ) {
			edges.remove(e)
		}
		
		forceClique(vs)
	}

	def eliminate(vs: List[V]) = {
		// TODO there is probably a more efficient way to do this
		for(v <- vs) {
			eliminate(v)
		}
	}

	class UndirectedVertexStringer(jung2pingel: Map[Vertex, V]) extends VertexStringer
	{

	    def getLabel(v: Vertex) = jung2pingel(v).getLabel()
	    
	}

	def draw() = {

        var jungGraph = new UndirectedSparseGraph()

        var pingel2jung = Map[V, Vertex]()
        var jung2pingel = Map[Vertex, V]()
        
        for( pv <- getVertices() ) {
        	val vertex = new SimpleUndirectedSparseVertex()
        	jungGraph.addVertex(vertex)
        	pingel2jung.put(pv, vertex)
        	jung2pingel.put(vertex, pv)
        }
        
        for( edge <- getEdges() ) {
        	val dbl = edge.getVertices()
        	val v1 = dbl._1
        	val v2 = dbl._2
        	val jedge = new UndirectedSparseEdge(pingel2jung.get(v1), pingel2jung.get(v2))
        	jungGraph.addEdge(jedge)
        }

        var pr = new PluggableRenderer()
//      pr.setVertexPaintFunction(new ModelVertexPaintFunction(m));
//      pr.setEdgeStrokeFunction(new ModelEdgeStrokeFunction(m));
//      pr.setEdgeShapeFunction(new EdgeShape.Line());
        pr.setVertexStringer(new UndirectedVertexStringer(jung2pingel))

        val layout = new FRLayout(jungGraph)
        
        var jf = new JFrame()
        GraphDraw gd = new GraphDraw(jungGraph)
        gd.getVisualizationViewer().setGraphLayout(layout)
        gd.getVisualizationViewer().setRenderer(pr)
        jf.getContentPane().add(gd)
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
        jf.pack()
        jf.setVisible(true)
	}

}
