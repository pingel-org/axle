package org.pingel.bayes;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DTree
{

	public Set<RandomVariable> cluster(DTreeNode n)
	{
		return null; // TODO 
	}
	
	public Set<RandomVariable> context(DTreeNode n)
	{
		return null; // TODO
	}
	
	public boolean isLeaf(DTreeNode n)
	{
		return false; // TODO
	}
	
	// returns an order pi with width(pi,G) no greater than the width
	// of dtree rooted at t
	
	public List<RandomVariable> toEliminationOrder(DTreeNode t)
	{
		List<RandomVariable> result = new ArrayList<RandomVariable>();

		if( isLeaf(t) ) {
			Set<RandomVariable> context = context(t);
			for(RandomVariable v : cluster(t)) {
				if( ! context.contains(v) ) {
					result.add(v);
				}
			}
		}
		else {
			List<RandomVariable> leftPi = null; // TODO
			List<RandomVariable> rightPi = null; // TODO
			// TODO merge them
			// TODO add cluster(t) - context(t) in any order to result
		}
		
		return result;
	}
	
}
