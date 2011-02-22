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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;

import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.VertexStringer;
import edu.uci.ics.jung.graph.impl.SimpleUndirectedSparseVertex;
import edu.uci.ics.jung.graph.impl.UndirectedSparseEdge;
import edu.uci.ics.jung.graph.impl.UndirectedSparseGraph;
import edu.uci.ics.jung.visualization.FRLayout;
import edu.uci.ics.jung.visualization.GraphDraw;
import edu.uci.ics.jung.visualization.Layout;
import edu.uci.ics.jung.visualization.PluggableRenderer;

public abstract class UndirectedGraph<V extends UndirectedGraphVertex<E>, E extends UndirectedGraphEdge<V>>
{

	private Set<V> vertices = new HashSet<V>();
	private Set<E> edges = new HashSet<E>();
	private Map<V,Set<E>> vertex2edges = new HashMap<V,Set<E>>();
	
	public UndirectedGraph() {
		
	}

	public void addVertex(V v) {
		vertices.add(v);
	}

	public Set<V> getVertices()
	{
		return vertices;
	}

    public int getNumVertices()
    {
        return vertices.size();
    }
    
	public Set<E> getEdges()
	{
		return edges;
	}
	
	// dissertation idea: how best to have an "Edge" object without storing them
	
	public void addEdge(E e) {
		
		// assume that this edge isn't already in our list of edges
		
		edges.add(e);
		Double<V,V> dble = e.getVertices();
		
		Set<E> es1 = getEdges(dble.getFirst());
		es1.add(e);
		Set<E> es2 = getEdges(dble.getSecond());
		es2.add(e);
	}

	public abstract E constructEdge(V v1, V v2);


    public void unlink(E e) 
    {
        Double<V,V> dble = e.getVertices();
        
        Set<E> es1 = getEdges(dble.getFirst());
        es1.remove(e);

        Set<E> es2 = getEdges(dble.getFirst());
        es2.remove(e);
        
        edges.remove(e);
    }
    
	public void unlink(V v1, V v2)
	{
	    // TODO optimize
	    
	    Set<E> edges = getEdges(v1);
	    
	    for( E edge : edges ) {
	        if( edge.other(v1).equals(v2) ) {
	            unlink(edge);
	        }
	    }
    }
    
	public boolean areNeighbors(V v1, V v2) {

		Set<E> es = getEdges(v1);
		for(E e : es) {
			if( e.connects(v1, v2) ) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isClique(Set<V> vs)
	{
		List<V> vList = new ArrayList<V>();
		vList.addAll(vs);
		
		for(int i=0; i < vList.size()-1; i++) {
			for(int j=0; j < vList.size(); j++) {
				if( ! areNeighbors(vList.get(i), vList.get(j)) ) {
					return false;
				}
			}
		}
		
		return true;
	}

	public int getNumEdgesToForceClique(Set<V> vs)
	{
		List<V> N = new ArrayList<V>();
		N.addAll(vs);

		int result = 0;
		
		for(int i=0; i < N.size() - 1; i++) {
			V vi = N.get(i);
			for(int j=i+1; j < N.size(); j++) {
				V vj = N.get(j);
				if( ! areNeighbors(vi, vj) ) {
					addEdge(constructEdge(vi, vj));
					result++;
				}
			}
		}

		return result;
	}
	
	public void forceClique(Set<V> vs) {
		
		List<V> vList = new ArrayList<V>();
		vList.addAll(vs);
		
		for(int i=0; i < vList.size() - 1; i++) {
			V vi = vList.get(i);
			for(int j=i+1; j < vList.size(); j++) {
				V vj = vList.get(j);
				if( ! areNeighbors(vi, vj) ) {
					addEdge(constructEdge(vi, vj));
				}
			}
		}
		
	}

	public V vertexWithFewestEdgesToEliminateAmong(Set<V> among)
	{
		// assert: among is a subset of vertices
		
		V result = null;
		int minSoFar = Integer.MAX_VALUE;
		
		for( V v : among ) {
			int x = getNumEdgesToForceClique(getNeighbors(v));
			if( result == null ) {
				result = v;
				minSoFar = x;
			}
			else if( x < minSoFar ) {
				result = v;
				minSoFar = x;
			}
		}

		return result;
	}

	public V vertexWithFewestNeighborsAmong(Set<V> among)
	{
		// assert: among is a subset of vertices
		
		V result = null;
		int minSoFar = Integer.MAX_VALUE;
		
		for( V v : among ) {
			int x = getNeighbors(v).size();
			if( result == null ) {
				result = v;
				minSoFar = x;
			}
			else if( x < minSoFar ) {
				result = v;
				minSoFar = x;
			}
		}
		
		return result;
	}
	
	public int degree(V v)
	{
		return getEdges(v).size();
	}

	public Set<E> getEdges(V v)
	{
		Set<E> result = new HashSet<E>();
		result = vertex2edges.get(v);
		if( result == null ) {
			result = new HashSet<E>();
			vertex2edges.put(v, result);
		}
		return result;
	}

	public Set<V> getNeighbors(V v)
	{
		Set<V> result = new HashSet<V>();
		
		Set<E> es = getEdges(v);
		for(E e : es) {
			result.add(e.other(v));
		}
		
		return result;
	}

	public abstract void copyTo(UndirectedGraph<V,E> other);
	
	public void delete(V v)
	{
		Set<E> es = getEdges(v);
		
		vertices.remove(v);
		vertex2edges.remove(v);
		for( E e : es ) {
			edges.remove(e);
			vertex2edges.get(e.other(v)).remove(e);
		}
	}

	public V firstLeafOtherThan(V r)
	{
		// a "leaf" is vertex with only one neighbor
		for(V v : vertices ) {
			if( getNeighbors(v).size() == 1 && ! v.equals(r) ) {
				return v;
			}
		}
		
		return null;
	}
	
	public void eliminate(V v)
	{
		// "decompositions" page 3 (Definition 3, Section 9.3)
		// turn the neighbors of v into a clique
		
		Set<E> es = getEdges(v);
		Set<V> vs = getNeighbors(v);

		vertices.remove(v);
		vertex2edges.remove(v);
		for( E e : es ) {
			edges.remove(e);
		}
		
		forceClique(vs);
		
	}

	public void eliminate(List<V> vs)
	{
		// TODO there is probably a more efficient way to do this
		
		for(V v : vs) {
			eliminate(v);
		}
	}


	class UndirectedVertexStringer implements VertexStringer
	{
		Map<Vertex, V> jung2pingel = null;
		
		UndirectedVertexStringer(Map<Vertex, V> jung2pingel)
		{
			this.jung2pingel = jung2pingel;
		}
		
	    public String getLabel(Vertex v)
	    {
	        return jung2pingel.get(v).getLabel();
	    }
		
	}

	public void draw()
	{

        UndirectedSparseGraph jungGraph = new UndirectedSparseGraph();

        Map<V, Vertex> pingel2jung = new HashMap<V, Vertex>();
        Map<Vertex, V> jung2pingel = new HashMap<Vertex, V>();
        
        for( V pv : getVertices() ) {
        		Vertex vertex = new SimpleUndirectedSparseVertex();
        		jungGraph.addVertex(vertex);
        		pingel2jung.put(pv, vertex);
        		jung2pingel.put(vertex, pv);
        }
        
        for( UndirectedGraphEdge<V> edge : getEdges() ) {
        	 	Double<V,V> dbl = edge.getVertices();
        	 	V v1 = dbl.getFirst();
        	 	V v2 = dbl.getSecond();
        	 	UndirectedSparseEdge jedge = new UndirectedSparseEdge(pingel2jung.get(v1), pingel2jung.get(v2));
        	 	jungGraph.addEdge(jedge);
        }

        PluggableRenderer pr = new PluggableRenderer();
//      pr.setVertexPaintFunction(new ModelVertexPaintFunction(m));
//      pr.setEdgeStrokeFunction(new ModelEdgeStrokeFunction(m));
//      pr.setEdgeShapeFunction(new EdgeShape.Line());
        pr.setVertexStringer(new UndirectedVertexStringer(jung2pingel));

        Layout layout = new FRLayout(jungGraph);
        
        JFrame jf = new JFrame();
        GraphDraw gd = new GraphDraw(jungGraph);
        gd.getVisualizationViewer().setGraphLayout(layout);
        gd.getVisualizationViewer().setRenderer(pr);
        jf.getContentPane().add(gd);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.pack();
        jf.setVisible(true);
	}

}
