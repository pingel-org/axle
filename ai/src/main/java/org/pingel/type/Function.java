package org.pingel.type;

public class Function extends Type {

	private Type from;
	private Type to;
	
	public Function(Type from, Type to)
	{
		this.from = from;
		this.to = to;
	}
	
	public String toString()
	{
		return from.toString() + " -> " + to.toString();
	}
	
}
