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

    var vertices = scala.collection.mutable.Set[V]()
    var edges = scala.collection.mutable.Set[E]()
    var vertex2outedges = scala.collection.mutable.Map[V, scala.collection.mutable.Set[E]]()
    var vertex2inedges = scala.collection.mutable.Map[V, scala.collection.mutable.Set[E]]()
    
    def addEdge(edge: E) = {
      
        val source = edge.getSource()
        val dest = edge.getDest()
        
        edges.add(edge)

        if( ! vertex2outedges.contains(source) ) {
          vertex2outedges += source -> scala.collection.mutable.Set[E]()
        }
        vertex2outedges(source).add(edge)

        if( ! vertex2inedges.contains(dest) ) {
          vertex2inedges += dest -> scala.collection.mutable.Set[E]()
        }
        vertex2inedges(dest).add(edge)
        
        edge
    }
    
    def getEdges() = edges

    def getVertices() = vertices

    def addVertex(v: V) = {
      vertices.add(v)
      v
    }

    def deleteEdge(e: E) = {

      edges.remove(e)
        
      vertex2outedges.get(e.getSource()) map { outwards =>
      	outwards.remove(e)
      }
        
      vertex2inedges.get(e.getDest()) map { inwards => 
      	inwards.remove(e)
      }
    }

    def deleteVertex(v: V)
    {
    	vertex2outedges.get(v) map { outEdges =>
    		for(e <- outEdges) {
    			edges.remove(e)
    			vertex2inedges.get(e.getDest()) map { out2in =>
    				out2in.remove(e)
    			}
    		}
    	}
    	vertex2outedges.remove(v)
    	
    	vertex2inedges.get(v) map { inEdges =>
    		for(e <- inEdges) {
    			edges.remove(e)
    			vertex2outedges.get(e.getSource()) map { in2out =>
    				in2out.remove(e)
    			}
    		}
    	}
    	vertex2inedges.remove(v)
    	
    	vertices.remove(v)
    }

    def getLeaves() = {
    	var result = scala.collection.mutable.Set[V]()
    	for( v <- getVertices() ) {
    		if( isLeaf(v) ) {
    			result.add(v)
    		}
    	}
    	result
    }
    
    def getNeighbors(v: V) = {
        var result = scala.collection.mutable.Set[V]()
        vertex2outedges.get(v) map { outEdges =>
            for( edge <- outEdges) {
                result.add(edge.getDest())
            }
        }
        vertex2inedges.get(v) map { inEdges =>
            for( edge <- inEdges) {
                result.add(edge.getSource())
            }
        }
        result
    }

    def precedes(v1: V, v2: V) = getPredecessors(v2).contains(v1)

    def getPredecessors(v: V) = {
        var result = scala.collection.mutable.Set[V]()
        vertex2inedges.get(v) map { inEdges =>
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
        var result = scala.collection.mutable.Set[V]()
        vertex2outedges.get(v) map { outEdges =>
            for( edge <- outEdges ) {
                result.add(edge.getDest())
            }
        }
        result
    }

    def outputEdgesOf(v: V) = {
        var result = scala.collection.mutable.Set[E]()
        vertex2outedges.get(v) map { outEdges => result ++= outEdges }
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
    
    def collectDescendants(v: V, result: scala.collection.mutable.Set[V]): Unit = {
        // inefficient
        if( ! result.contains(v) ) {
            result.add(v)
            for( child <- getSuccessors(v) ) {
                collectDescendants(child, result)
            }
        }
    }

    def collectAncestors(v: V, result: scala.collection.mutable.Set[V]): Unit = {
        // inefficient
        if( ! result.contains(v) ) {
            result.add(v)
            for( child <- getPredecessors(v) ) {
                collectAncestors(child, result)
            }
        }
    }
    
    def collectAncestors(vs: Set[V], result: scala.collection.mutable.Set[V]): Unit = {
        for( v <- vs ) {
            collectAncestors(v, result)
        }
    }
    
    def removeInputs(vs: Set[V]) {
        for( v <- vs ) {
            vertex2inedges.get(v) map { incoming =>
                for( edge <- incoming ) {
                    edges.remove(edge)
                }
                vertex2inedges += v -> null
            }
        }
    }

    def removeOutputs(vs: Set[V]) {
        for( v <- vs) {
            vertex2outedges.get(v) map { outgoing =>
                for( edge <- outgoing ) {
                    edges.remove(edge)
                }
                vertex2outedges += v -> null
            }
        }
    }

    //TODO remove this method
    def removeSuccessor(v: V, successor: V) {
        vertex2outedges.get(v) map { outgoing =>
          	outgoing.find({_.getDest().equals(successor)}) map { edgeToRemove =>
                outgoing.remove(edgeToRemove)
                edges.remove(edgeToRemove)
          	}
        }
    }

    //TODO remove this method
    def removePredecessor(v: V, predecessor: V) {
        vertex2inedges.get(v) map { incoming =>
            incoming.find({_.getSource().equals(predecessor)}) map { edgeToRemove =>
                incoming.remove(edgeToRemove)
                edges.remove(edgeToRemove) // we should really only do this if it's the last of the pair of calls. ick.
            }
        }
    }

    def moralGraph(): UndirectedGraph[_, _] = null // TODO !!!

    def isAcyclic() = true // TODO !!!

}
