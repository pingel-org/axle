package org.pingel.gestalt.core;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class Unifier {

    private Map<Name, Form> name2form = new TreeMap<Name, Form>();

    public Unifier() {}

    public Form get(Name name)
    {
		return name2form.get(name);
    }

    public void put(Name name, Form f)
    {
		name2form.put(name, f);
    }

    public String toString()
    {
		String result = "{\n";

		Iterator<Name> keyIt = name2form.keySet().iterator();
		while( keyIt.hasNext() ) {
		    Name n = keyIt.next();
		    Form f = name2form.get(n);
		    result += n + " -> " + f.toString() + "\n";
		}
		result += " }";

		return result;
    }
}
