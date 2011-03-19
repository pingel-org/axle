package org.pingel.bayes;

import java.util.Set;

public class Independence {

	public Set<RandomVariable> X;
	public Set<RandomVariable> Y;
	public Set<RandomVariable> Z;
	
	// this is read "X is independent of Y given Z"
	public Independence(Set<RandomVariable> X, Set<RandomVariable> Z, Set<RandomVariable> Y)
	{
		this.X = X;
		this.Z = Z;
		this.Y = Y;
	}

	private String variablesToString(Set<RandomVariable> s)
	{
		String result = "{";
		for( RandomVariable var : s ) {
			result += var.name;
		}
		result += "}";
		
		return result;
	}
	
	public String toString()
	{
		return "I(" +
		variablesToString(X) + ", " +
		variablesToString(Z) + ", " +
		variablesToString(Y) + ")";
	}
	
}
