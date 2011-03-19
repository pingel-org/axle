package org.pingel.bayes;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.pingel.util.DirectedGraph;
import org.pingel.util.Lister;

public class Model
{
	
	private ModelGraph graph = new ModelGraph();
	protected int newVarIndex = 0;
	protected Map<String, RandomVariable> name2variable = new HashMap<String, RandomVariable>();
	protected String name = "unknown";
	
	public Model()
	{
	}
	
	public Model(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void copyTo(Model other)
	{
		other.name = name;
		
		for(RandomVariable var : variables ) {
			other.addVariable(var);
		}
		
		for(ModelEdge edge : graph.getEdges() ) {
			other.connect(edge.getSource(), edge.getDest());
		}
		
	}
	
	public DirectedGraph<RandomVariable, ModelEdge> getGraph()
	{
		return graph;
	}
	
	public void connect(RandomVariable source, RandomVariable dest)
	{
		graph.addEdge(new ModelEdge(source, dest));
	}
	
	private List<RandomVariable> variables = new ArrayList<RandomVariable>();
	
	
	public RandomVariable addVariable(RandomVariable var)
	{
		variables.add(var);
		name2variable.put(var.name, var);
		return graph.addVertex(var);
	}
	
	public List<RandomVariable> getRandomVariables()
	{
		return variables;
	}
	
	public RandomVariable getVariable(String name)
	{
		return name2variable.get(name);
	}

	public void deleteVariable(RandomVariable var)
	{
		variables.remove(var);
		graph.deleteVertex(var);
	}
	
	public int numVariables()
	{
		return variables.size();
	}
	
	public boolean blocks(Set<RandomVariable> from, Set<RandomVariable> to, Set<RandomVariable> given)
	{
		List<RandomVariable> path = _findOpenPath(new HashMap<RandomVariable, Set<RandomVariable>>(), UNKNOWN, null, from, to, given);
		return path == null;
	}
	
	final static int UNKNOWN = 0;
	final static int OUTWARD = -1;
	final static int INWARD = 1;
	
	Lister<RandomVariable, String> rvNameGetter = new Lister<RandomVariable, String>() {
		public String function(RandomVariable rv) {
			return rv.name;
		}
	};
	
	private List<RandomVariable> _findOpenPath(
			Map<RandomVariable, Set<RandomVariable>> visited,
			int priorDirection,
			RandomVariable prior,
			Set<RandomVariable> current,
			Set<RandomVariable> to,
			Set<RandomVariable> given)
			{
		
//		System.out.println("_fOP: " + priorDirection +
//		", prior = " + ((prior == null ) ? "null" : prior.name) +
//		", current = " + rvNameGetter.execute(current) +
//		", to = " + rvNameGetter.execute(to) +
//		", evidence = " + rvNameGetter.execute(given));
		
		Set<RandomVariable> cachedOuts = visited.get(prior);
		if( cachedOuts != null ) {
			current.removeAll(cachedOuts);
		}
		
		for( RandomVariable var : current ) {
			
			boolean openToVar = false;
			int directionPriorToVar = UNKNOWN;
			if( prior == null ) {
				openToVar = true;
			}
			else {
				directionPriorToVar = OUTWARD;
				if( getGraph().precedes(var, prior) ) {
					directionPriorToVar = INWARD;
				}
				
				if( priorDirection != UNKNOWN ) {
					boolean priorGiven = given.contains(prior);
					openToVar =
						(priorDirection == INWARD  && ! priorGiven                                  && directionPriorToVar == OUTWARD) ||
						(priorDirection == OUTWARD && ! priorGiven                                  && directionPriorToVar == OUTWARD) ||
						(priorDirection == INWARD  && graph.descendantsIntersectsSet(var, given) && directionPriorToVar == INWARD);
				}
				else {
					openToVar = true;
				}
			}
			
			if( openToVar ) {
				if( to.contains(var) ) {
					Vector<RandomVariable> path = new Vector<RandomVariable>();
					path.add(var);
					return path;
				}
				Set<RandomVariable> neighbors = graph.getNeighbors(var);
				neighbors.remove(prior);
				
				Map<RandomVariable, Set<RandomVariable>> visitedCopy = new HashMap<RandomVariable, Set<RandomVariable>>();
				visitedCopy.putAll(visited);
				Set<RandomVariable> outs = visited.get(prior);
				if( outs == null ) {
					outs = new HashSet<RandomVariable>();
					visitedCopy.put(prior, outs);
				}
				outs.add(var);
				
				List<RandomVariable> path = _findOpenPath(visitedCopy, -1 * directionPriorToVar, var, neighbors, to, given);
				if( path != null ) {
					path.add(var);
					return path;
				}
			}
		}
		return null;
			}
	
}
