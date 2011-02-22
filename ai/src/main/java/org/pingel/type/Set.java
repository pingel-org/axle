package org.pingel.type;

public class Set extends Type {

	private Type memberType;
	
	public Set(Type memberType)
	{
		this.memberType = memberType;
	}
	
}
