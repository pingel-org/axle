package org.pingel.type;

import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import org.pingel.gestalt.core.Form;

public class Type {

    private List<Form> values = new Vector<Form>();
    private TreeMap<Form,Integer> values2index = new TreeMap<Form,Integer>();

    public List<Form> getValues() {
        return values;
    }

    public void addValue(Form val) {
        int index = values.size();
        values.add(val);
        values2index.put(val, new Integer(index));
    }
    
    public int indexOf(Form val) {
        Integer i = values2index.get(val);
        return i.intValue();
    }
    
}
