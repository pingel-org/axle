package org.pingel.bayes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pingel.util.DirectedGraph;
import org.pingel.util.UndirectedGraph;

public class JoinTree
extends UndirectedGraph<JoinTreeNode, JoinTreeEdge>
{
	
	Map<JoinTreeNode, Set<RandomVariable>> node2cluster = new HashMap<JoinTreeNode, Set<RandomVariable>>();

	public void copyTo(UndirectedGraph<JoinTreeNode, JoinTreeEdge> other)
	{
		// asdf();
	}

	public void setCluster(JoinTreeNode n, Set<RandomVariable> cluster)
	{
		node2cluster.put(n, cluster);
	}

	public void addToCluster(JoinTreeNode n, RandomVariable v)
	{
		Set<RandomVariable> cluster = node2cluster.get(n);
		if( cluster == null ) {
			cluster = new HashSet<RandomVariable>();
			node2cluster.put(n, cluster);
		}
		cluster.add(v);
	}
	
	public JoinTreeEdge constructEdge(JoinTreeNode n1, JoinTreeNode n2)
	{
		return new JoinTreeEdge(n1, n2);
	}

	public Set<RandomVariable> separate(JoinTreeNode n1, JoinTreeNode n2)
	{
		Set<RandomVariable> result = new HashSet<RandomVariable>();
		
		for(RandomVariable v : node2cluster.get(n1)) {
			if( node2cluster.get(n2).contains(v) ) {
				result.add(v);
			}
		}
		return result;
	}

	public static JoinTree fromEliminationOrder(DirectedGraph G, List<RandomVariable> pi)
	{
		// returns a jointree for DAG G with width equal to width(pi, G)
		
		JoinTree T = new JoinTree();

		UndirectedGraph Gm = G.moralGraph();

		List<Set<RandomVariable>> clusterSequence = null; // Gm.induceClusterSequence(pi);
		
		
		
		return T;
	}
	
	public List<RandomVariable> toEliminationOrder(JoinTreeNode r)
	{
		List<RandomVariable> result = new ArrayList<RandomVariable>();
		
		JoinTree T = new JoinTree();
		copyTo(T); // not yet implemented
		
		while( T.getVertices().size() > 1 ) {
			JoinTreeNode i = T.firstLeafOtherThan(r);
			JoinTreeNode j = null; // TODO theNeighbor();
			for(RandomVariable v : node2cluster.get(i)) {
				if( ! node2cluster.get(j).contains(v) ) {
					result.add(v);
				}
			}
		}

		for(RandomVariable v : node2cluster.get(r) )  {
			result.add(v);
		}
		
		return result;
	}
	
	public boolean embeds(EliminationTree eTree, Map<JoinTreeNode, EliminationTreeNode> embedding)
	{
		for(JoinTreeNode jtn : getVertices() ) {
			Set<RandomVariable> cluster = node2cluster.get(jtn);
			EliminationTreeNode etn = embedding.get(jtn);
			for(RandomVariable v : eTree.getFactor(etn).getVariables() ) {
				if( ! cluster.contains(v) ) {
					return false;
				}
			}
		}
		
		return true;
	}
	
}
