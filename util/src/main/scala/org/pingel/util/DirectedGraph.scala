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

package org.pingel.util;


class DirectedGraphEdge[V](source: V, dest: V) {

  def getSource() = source
  
  def getDest() = dest

}

trait DirectedGraphVertex[E] {

}

class DirectedGraph[V <: DirectedGraphVertex[E], E <: DirectedGraphEdge[V]] {

    var vertices = Set[V]()
    var edges = Set[E]()
    var vertex2outedges = Map[V, Set[E]]()
    var vertex2inedges = Map[V, Set[E]]()
    
    def addEdge(edge: E) = {
      
        val source = edge.getSource()
        val dest = edge.getDest()
        
        edges.add(edge)
        
        var outEdges = vertex2outedges.get(source)
        if( outEdges == null ) {
            outEdges = Set[E]()
            vertex2outedges += source -> outEdges
        }
        outEdges.add(edge)
        
        var inEdges = vertex2inedges.get(dest)
        if( inEdges == null ) {
            inEdges = Set[E]()
            vertex2inedges += dest -> inEdges
        }
        inEdges.add(edge)
        
        edge
    }
    
    def getEdges() = edges

    def getVertices = vertices

    def addVertex(v: V) = {
      vertices.add(v)
      v
    }

    def deleteEdge(e: E) = {

      edges.remove(e)
        
      var outwards = vertex2outedges.get(e.getSource())
      outwards.remove(e)
        
      var inwards = vertex2inedges.get(e.getDest())
      inwards.remove(e)
    }

    def deleteVertex(v: V)
    {
    	val outEdges = vertex2outedges.get(v);
    	if( outEdges != null ) {
    		for(e <- outEdges) {
    			edges.remove(e)
    			var out2in = vertex2inedges.get(e.getDest())
    			out2in.remove(e)
    		}
    	}
    	vertex2outedges.remove(v)
    	
    	val inEdges = vertex2inedges.get(v)
    	if( inEdges != null ) {
    		for(e <- inEdges) {
    			edges.remove(e)
    			var in2out = vertex2outedges.get(e.getSource())
    			in2out.remove(e)
    		}
    	}
    	vertex2inedges.remove(v)
    	vertices.remove(v)
    }

    def getLeaves() = {
    	var result = Set[V]()
    	for( v <- getVertices() ) {
    		if( isLeaf(v) ) {
    			result.add(v)
    		}
    	}
    	result
    }
    
    def getNeighbors(v: V) = {
        var result = Set[V]()
        val outEdges = vertex2outedges(v)
        if( outEdges != null ) {
            for( edge <- outEdges) {
                result.add(edge.getDest());
            }
        }
        val inEdges = vertex2inedges(v)
        if( inEdges != null ) {
            for( edge <- inEdges) {
                result.add(edge.getSource())
            }
        }
        result
    }

    def precedes(v1: V, v2: V) = getPredecessors(v2).contains(v1)

    def getPredecessors(v: V) = {
        var result = Set[V]()
        val inEdges = vertex2inedges.get(v)
        if( inEdges != null ) {
            for( edge <- inEdges ) {
                result.add(edge.getSource())
            }
        }
        result
    }

    def isLeaf(v: V) = {
    	val outEdges = vertex2outedges.get(v)
    	outEdges == null || outEdges.size == 0
    }
    
    def getSuccessors(v: V) = {
        var result = Set[V]()
        val outEdges = vertex2outedges.get(v)
        if( outEdges != null ) {
            for( edge <- outEdges ) {
                result.add(edge.getDest())
            }
        }
        result
    }

    def outputEdgesOf(v: V) = {
        var result = Set[E]()
        val outEdges = vertex2outedges.get(v)
        if( outEdges != null ) {
            result.addAll(outEdges)
        }
        result
    }

    def descendantsIntersectsSet(v: V, s: Set[V]): Boolean = {
      
        if( s.contains(v) ) {
            return true
        }
        for( x <- s ) {
            if( descendantsIntersectsSet(x, s) ) {
                return true
            }
        }
        return false
    }
    
    def collectDescendants(v: V, result: Set[V]): Unit = {
        // inefficient
        if( ! result.contains(v) ) {
            result.add(v)
            for( child <- getSuccessors(v) ) {
                collectDescendants(child, result)
            }
        }
    }

    def collectAncestors(v: V, result: Set[V]): Unit = {
        // inefficient
        if( ! result.contains(v) ) {
            result.add(v)
            for( child <- getPredecessors(v) ) {
                collectAncestors(child, result)
            }
        }
    }
    
    def collectAncestors(vs: Set[V], result: Set[V]): Unit = {
        for( v <- vs ) {
            collectAncestors(v, result)
        }
    }
    
    def removeInputs(vs: Set[V]) {
        for( v <- vs ) {
            val incoming = vertex2inedges.get(v)
            if( incoming != null ) {
                for( edge <- incoming ) {
                    edges.remove(edge)
                }
                vertex2inedges += v -> null
            }
        }
    }

    def removeOutputs(vs: Set[V]) {
        for( v <- vs) {
            val outgoing = vertex2outedges.get(v)
            if( outgoing != null ) {
                for( edge <- outgoing ) {
                    edges.remove(edge)
                }
                vertex2outedges += v -> null
            }
        }
    }

    //TODO remove this method
    def removeSuccessor(v: V, successor: V) {
        
        val outgoing = vertex2outedges.get(v)
        if( outgoing != null ) {

            var edgeToRemove: E = null
            
            for( edge <- outgoing ) {
                if( edge.getDest().equals(successor) ) {
                    edgeToRemove = edge
                }
            }
            
            if( edgeToRemove != null ) {
                outgoing.remove(edgeToRemove)
                edges.remove(edgeToRemove)
            }
        }

    }

    //TODO remove this method
    def removePredecessor(v: V, predecessor: V)
    {
        val incoming = vertex2inedges.get(v)
        if( incoming != null ) {
            var edgeToRemove: E = null
            for( edge <- incoming ) {
                if( edge.getSource().equals(predecessor)) {
                    edgeToRemove = edge
                }
            }
            
            if( edgeToRemove != null ) {
                incoming.remove(edgeToRemove)
                edges.remove(edgeToRemove) // we should really only do this if it's the last of the pair of calls. ick.
            }

        }

    }

    def moralGraph(): UndirectedGraph[_, _] = null // TODO !!!

    def isAcyclic() = true // TODO !!!

}
