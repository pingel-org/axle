package org.pingel.gestalt.core;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Lambda {

	TreeSet<Name> names = new TreeSet<Name>();
    Map<Name, Name> name2type = new TreeMap<Name, Name>();
	
	public Lambda() { }

	public Set<Name> getNames()
	{
		return names;
	}
	
	public boolean contains(Name n)
	{
		return names.contains(n);
	}
    private static Name top = new Name("top");
    
    public void add(Name var)
    {
        add(var, top);
    }
    
	public void add(Name var, Name type)
	{
		names.add(var);
        name2type.put(var, type);
	}
	
	public void addAll(Lambda other)
	{
		names.addAll(other.getNames());
	}
}
