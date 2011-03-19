package org.pingel.bayes;

import java.util.ArrayList;
import java.util.List;

import org.pingel.util.UndirectedGraph;

public class InteractionGraph extends UndirectedGraph<RandomVariable,VariableLink> {

	public VariableLink constructEdge(RandomVariable v1, RandomVariable v2)
	{
		return new VariableLink(v1, v2);
	}

	public void copyTo(UndirectedGraph<RandomVariable, VariableLink> other)
	{
		// should this be shallow or deep copies of the vertex/edge sets
		
		for(RandomVariable rv : getVertices() ) {
			other.addVertex(rv);
		}
		
		for(VariableLink vl : getEdges() ) {
			other.addEdge(vl);
		}
	}

	public List<InteractionGraph> eliminationSequence(List<RandomVariable> pi) {

		List<InteractionGraph> result = new ArrayList<InteractionGraph>();
		InteractionGraph G = this;
		result.add(G);
		
		for( RandomVariable rv : pi ) {
			InteractionGraph newG = new InteractionGraph();
			G.copyTo(newG);
			newG.eliminate(rv);
			result.add(newG);
			G = newG;
		}
		
		return result;
	}

}
