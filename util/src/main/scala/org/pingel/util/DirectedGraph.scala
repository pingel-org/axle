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
    			Set<E> out2in = vertex2inedges.get(e.getDest())
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

    public Set<V> getLeaves()
    {
    		Set<V> result = new HashSet<V>();
    		for( V v : getVertices() ) {
    			if( isLeaf(v) ) {
    				result.add(v);
    			}
    		}
    		return result;
    }
    
    public Set<V> getNeighbors(V v)
    {
        Set<V> result = new HashSet<V>();
        Set<E> outEdges = vertex2outedges.get(v);
        if( outEdges != null ) {
            for( E edge : outEdges) {
                result.add(edge.getDest());
            }
        }
        Set<E> inEdges = vertex2inedges.get(v);
        if( inEdges != null ) {
            for( E edge : inEdges) {
                result.add(edge.getSource());
            }
        }
        
        return result;
    }

    public boolean precedes(V v1, V v2)
    {
        Set<V> preds = getPredecessors(v2);
        return preds.contains(v1);
    }
    
    public Set<V> getPredecessors(V v)
    {
        Set<V> result = new HashSet<V>();
        Set<E> inEdges = vertex2inedges.get(v);
        if( inEdges != null ) {
            for( E edge : inEdges ) {
                result.add(edge.getSource());
            }
        }
        return result;
    }

    public boolean isLeaf(V v)
    {
    		Set<E> outEdges = vertex2outedges.get(v);
    		return outEdges == null || outEdges.size() == 0;

    }
    
    public Set<V> getSuccessors(V v)
    {
        Set<V> result = new HashSet<V>();
        Set<E> outEdges = vertex2outedges.get(v);
        if( outEdges != null ) {
            for( E edge : outEdges ) {
                result.add(edge.getDest());
            }
        }
        return result;
    }

    public Set<E> outputEdgesOf(V v)
    {
        Set<E> result = new HashSet<E>();
        Set<E> outEdges = vertex2outedges.get(v);
        if( outEdges != null ) {
            result.addAll(outEdges);
        }
        return result;
    }
    
    
    public boolean descendantsIntersectsSet(V var, Set<V> s)
    {
        if( s.contains(var) ) {
            return true;
        }
        for( V x : s ) {
            if( descendantsIntersectsSet(x, s) ) {
                return true;
            }
        }
        return false;
    }
    
    public void collectDescendants(V v, Set<V> result)
    {
        // inefficient
        if( ! result.contains(v) ) {
            result.add(v);
            for( V child : getSuccessors(v) ) {
                collectDescendants(child, result);
            }
        }
    }

    
    public void collectAncestors(V v, Set<V> result)
    {
        // inefficient
        if( ! result.contains(v) ) {
            result.add(v);
            for( V child : getPredecessors(v) ) {
                collectAncestors(child, result);
            }
        }
    }
    
    public void collectAncestors(Set<V> vs, Set<V> result)
    {
        for( V v : vs ) {
            collectAncestors(v, result);
        }
    }
    
    public void removeInputs(Set<V> vs)
    {
        for( V v : vs ) {
            Set<E> incoming = vertex2inedges.get(v);
            if( incoming != null ) {
                for( E edge : incoming ) {
                    edges.remove(edge);
                }
                vertex2inedges.put(v, null);
            }
        }
    }

    public void removeOutputs(Set<V> vs)
    {
        for( V v : vs) {
            Set<E> outgoing = vertex2outedges.get(v);
            if( outgoing != null ) {
                for( E edge : outgoing ) {
                    edges.remove(edge);
                }
                vertex2outedges.put(v, null);
            }
        }
    }

    //TODO remove this method
    public void removeSuccessor(V v, V successor)
    {
        
        Set<E> outgoing = vertex2outedges.get(v);
        if( outgoing != null ) {

            E edgeToRemove = null;
            
            for( E edge : outgoing ) {
                if( edge.getDest().equals(successor) ) {
                    edgeToRemove = edge;
                }
            }
            
            if( edgeToRemove != null ) {
                outgoing.remove(edgeToRemove);
                edges.remove(edgeToRemove);
            }
        }

    }

    //TODO remove this method
    public void removePredecessor(V v, V predecessor)
    {
        Set<E> incoming = vertex2inedges.get(v);
        if( incoming != null ) {
            E edgeToRemove = null;
            for( E edge : incoming ) {
                if( edge.getSource().equals(predecessor)) {
                    edgeToRemove = edge;
                }
            }
            
            if( edgeToRemove != null ) {
                incoming.remove(edgeToRemove);
                edges.remove(edgeToRemove); // we should really only do this if it's the last of the pair of calls. ick.
            }

        }

    }

    public UndirectedGraph moralGraph()
    {
        return null; // TODO
    }
    
    public boolean isAcyclic()
    {
        return true; // TODO !!!
    }
    
}
