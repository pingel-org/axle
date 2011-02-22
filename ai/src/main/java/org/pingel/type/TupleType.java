package org.pingel.type;

import java.util.Arrays;
import java.util.List;

public class TupleType extends Type {

	List<Type> types;
	
	public TupleType(Type...types)
	{
		this.types = Arrays.asList(types);
	}
	
}
