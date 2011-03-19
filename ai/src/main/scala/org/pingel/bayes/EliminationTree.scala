package org.pingel.bayes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.pingel.util.UndirectedGraph;

public class EliminationTree
extends UndirectedGraph<EliminationTreeNode, EliminationTreeEdge>
{
	
	Map<EliminationTreeNode, Factor> node2phi = new HashMap<EliminationTreeNode, Factor>();
	
	public EliminationTree()	{ }

	private void gatherVars(EliminationTreeNode stop, EliminationTreeNode node, Set<RandomVariable> result)
	{
		result.addAll(node2phi.get(node).getVariables());
		for(EliminationTreeNode n : getNeighbors(node)) {
			if( ! n.equals(stop) ) {
				gatherVars(node, n, result);
			}
		}
		
	}
	
	public Set<RandomVariable> cluster(EliminationTreeNode i) 
	{
		Set<RandomVariable> result = new HashSet<RandomVariable>();
		
		for(EliminationTreeNode j : getNeighbors(i)) {
			result.addAll(separate(i, j));
		}
		
		result.addAll(node2phi.get(i).getVariables());
		
		return result;
	}
	
	public Set<RandomVariable> separate(EliminationTreeNode i, EliminationTreeNode j)
	{
		Set<RandomVariable> iSide = new HashSet<RandomVariable>();
		gatherVars(j, i, iSide);
		
		Set<RandomVariable> jSide = new HashSet<RandomVariable>();
		gatherVars(i, j, jSide);
		
		Set<RandomVariable> result = new HashSet<RandomVariable>();
		for(RandomVariable iv : iSide) {
			if( jSide.contains(iv) ) {
				result.add(iv);
			}
		}
		
		return result;
	}
	
	public EliminationTreeEdge constructEdge(EliminationTreeNode v1, EliminationTreeNode v2)
	{
		return new EliminationTreeEdge(v1, v2);
	}

	public void delete(EliminationTreeNode node)
	{
		super.delete(node);
		node2phi.remove(node);
	}
	
	public Set<RandomVariable> getAllVariables()
	{
		Set<RandomVariable> result = new HashSet<RandomVariable>();
		
		for(EliminationTreeNode node : node2phi.keySet()) {
			Factor f = node2phi.get(node);
			result.addAll(f.getVariables());
		}
		
		return result;
	}
	
	public void addFactor(EliminationTreeNode node, Factor f)
	{
		Factor existing = node2phi.get(node);
		if( existing == null ) {
			node2phi.put(node, f);
		}
		else {
			node2phi.put(node, existing.multiply(f));
		}
	}

	public Factor getFactor(EliminationTreeNode node)
	{
		return node2phi.get(node);
	}
	
	public void setFactor(EliminationTreeNode node, Factor f)
	{
		node2phi.put(node, f);
	}
	
	public void copyTo(UndirectedGraph<EliminationTreeNode, EliminationTreeEdge> other)
	{
		
		for(EliminationTreeNode node : getVertices() ) {
			other.addVertex(node);
		}
		
		for(EliminationTreeEdge edge : getEdges() ) {
			other.addEdge(edge);
		}

		for(EliminationTreeNode node : node2phi.keySet() ) {
			((EliminationTree) other).setFactor(node, node2phi.get(node));
		}
		
	}
	
}
