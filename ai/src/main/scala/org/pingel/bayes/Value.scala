package org.pingel.bayes;

public class Value
implements Comparable<Value>
{

	private String v = null;
	
	public Value(String v)
	{
		this.v = v;
	}
	
	public int compareTo(Value other)
	{
		return v.compareTo(other.v);
	}
	
	public String toString()
	{
		return v;
	}
}

