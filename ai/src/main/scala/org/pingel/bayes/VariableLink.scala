package org.pingel.bayes;

import org.pingel.util.UndirectedGraphEdge;

public class VariableLink extends UndirectedGraphEdge<RandomVariable> {

	public VariableLink(RandomVariable v1, RandomVariable v2) {
		super(v1, v2);
	}

}
