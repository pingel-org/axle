package org.pingel.bayes;

import java.util.ArrayList;
import java.util.List;

public class Domain {

	List<Value> values;
	
	public Domain(Value... vs)
	{
		values = new ArrayList<Value>();
		for(Value v : vs) {
			values.add(v);
		}
	}

    protected void addValue(Value v)
    {
        values.add(v);
    }
    
	public List<Value> getValues()
	{
		return values;
	}
}
