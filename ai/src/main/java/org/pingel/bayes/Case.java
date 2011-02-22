package org.pingel.bayes;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

public class Case implements Comparable<Case>
{
	private Map<RandomVariable,Value> assignments = new TreeMap<RandomVariable, Value>();

	public Case() {}

	public Case(RandomVariable var, Value val)
	{
	    assign(var, val);
	}

	public Set<RandomVariable> getVariables()
	{
	    return assignments.keySet();
	}
	
	public int size()
	{
		return assignments.keySet().size();
	}

	public Value valueOf(RandomVariable var) {
		return assignments.get(var);
	}

	public List<Value> valuesOf(List<RandomVariable> vars) {

	    // Note: this may contain null entries if assignments.keySet()
	    // is a strict subset of vars
	    
	    List<Value> result = new Vector<Value>();
	    for(int i=0; i < vars.size(); i++) {
	        result.add(assignments.get(vars.get(i)));
	    }
	    return result;
	}
	
	public void assign(RandomVariable rv, Value val)
	{
		assignments.put(rv, val);
	}

	public void assign(List<RandomVariable> vars, List<Value> vals)
	{
	    for(int i=0; i < vars.size(); i++ ) {
	        RandomVariable var = vars.get(i);
	        Value val = vals.get(i);
			assignments.put(var, val);
	    }
	}

	public boolean isSupersetOf(Case other)
	{
		Iterator<RandomVariable> it = other.assignments.keySet().iterator();
		while( it.hasNext() ) {
		    RandomVariable var = it.next();
		    Value otherVal = other.valueOf(var);
		    Value thisVal = valueOf(var);
			if ( otherVal != null &&
				 thisVal != null &&
				 ! thisVal.equals(otherVal) ) {
				return false;
			}
		}
		return true;
	}
	
	public Case copy()
	{
		Case result = new Case();
        result.assignments = new TreeMap<RandomVariable, Value>();
        result.assignments.putAll(assignments);
		return result;
	}

	public Case projectToVars(List<RandomVariable> pVars)
	{
		Case result = new Case();

		for( RandomVariable var : pVars ) {
			result.assign(var, valueOf(var));
		}
		
		return result;
	}

	public boolean equals(Object o) {
        if( o instanceof Case ) {
            return compareTo((Case)o) == 0;
        }
        else {
            return false;
        }
	}
	
	public int compareTo(Case other)
	{
	    if( assignments.size() < other.assignments.size() ) {
			return -1;
		}
		if( assignments.size() > other.assignments.size() ) {
			return 1;
		}
		
		for( RandomVariable var : assignments.keySet() ) {
			Value myValue = assignments.get(var);
			Value otherValue = other.assignments.get(var);
			
			if( ! myValue.equals(otherValue) ) {
				return myValue.compareTo(otherValue);
			}
		}

		return 0;
	}
	
    public String toString()
    {
        String result = "";
        for( RandomVariable rv : assignments.keySet() ) {
            // System.out.println("rv = " + rv.name);
            result += rv.name + " = ";
            if(  assignments.get(rv) == null ) {
                result += "null";
            }
            else {
                result += assignments.get(rv).toString();
            }
            result += ", ";
        }
        return result;
    }
    
    public String toOrderedString(RandomVariable... rvs)
    {
    		String result = "";
    	
    		for(int i=0; i < rvs.length; i++ ) {
    			result += rvs[i].name + " = " + assignments.get(rvs[i]);
    			if( i < rvs.length - 1 ) {
    				result += ", ";
    			}
    		}
    		
    		return result;
    }
    
    public String toOrderedString(List<RandomVariable> vs)
    {
    		String result = "";
		
		Iterator<RandomVariable> it = vs.iterator();
		for(int i=0; i < vs.size(); i++ ) {
			RandomVariable var = it.next();
			result += var.name + " = " + assignments.get(var);
			if( it.hasNext() ) {
				result += ", ";
			}
		}
		
		return result;
	}
}
