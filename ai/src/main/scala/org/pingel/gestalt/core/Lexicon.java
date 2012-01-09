package org.pingel.gestalt.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.pingel.util.Printable;

public class Lexicon
{
    private Map<Name, Logos> name2object = new TreeMap<Name, Logos>();
    private Map<Logos, Name> object2name = new HashMap<Logos, Name>(); // this may be buggy

    Set<FormFactory> formFactories = new HashSet<FormFactory>();
    Set<TransformFactory> transformFactories = new HashSet<TransformFactory>();
    
    
    public Lexicon() {}
    
    public void put(Name name, Logos o)
    {
    	GLogger.global.info("putting " + name + " of class " + o.getClass() + " = " + o.toString());
    	
    	/*
    	 Situation already = (Situation) name2object.get(name.base);
    	 if( already != null ) {
    	 System.out.println("addSymbol: there is already a node with base name " + name.base);
    	 System.exit(1);
    	 }
    	 */
    	
    	name2object.put(name, o);
    	object2name.put(o, name);
    }

    
    public void addFactories()
    {
        for( Form form : getTopForms() ) {
            formFactories.add(new FormFactory(form));
        }
        
        for( Transform t : getTransforms() ) {
            transformFactories.add(new TransformFactory(t, this));
        }            
        
        Blank leftBlank = new Blank();
        Blank rightBlank = new Blank();
        ComplexForm cf = new ComplexForm(leftBlank, rightBlank);
        leftBlank.setParent(cf);
        rightBlank.setParent(cf);
        formFactories.add(new FormFactory(cf));
    }
    
    
    public void addFormFactory(FormFactory ff)
    {
        formFactories.add(ff);
    }

    public Set<FormFactory> getFormFactories()
    {
        return formFactories;
    }
    
    public void addTransformFactory(TransformFactory tf)
    {
        transformFactories.add(tf);
    }

    public Set<TransformFactory> getTransformFactories()
    {
        return transformFactories;
    }
    
    public Set<Name> getNames()
    {
        return name2object.keySet();
    }
    
    public Logos get(Name name)
    {
    	return name2object.get(name);
    }

    public Transform getTransform(Name name)
    {
    	GLogger.global.info("getTransform(" + name + ")");
    	
    	Transform t = (Transform) name2object.get(name);
    	return t;
    }

    public void renameTransform(Name from, Name to)
    {
    	Transform t = (Transform) name2object.get(from);
    	name2object.remove(from);
    	object2name.remove(t);
    	name2object.put(to, t);
    	object2name.put(t, to);
    }

    public void renameForm(Name from, Name to)
    {
    	Form f = (Form) name2object.get(from);
    	name2object.remove(from);
    	object2name.remove(f);
    	name2object.put(to, f);
    	object2name.put(f, to);
    }
    
    public Form getForm(Name name)
    {
    	GLogger.global.info("getForm(" + name + ")");
    	
    	Form f = (Form) name2object.get(name);
    	return f;
    }
    
    public void remove(Logos logos)
    {
        Name name = object2name.get(logos);
        if( name != null ) {
            object2name.remove(logos);
            name2object.remove(name);
        }
    }
    
    public Set<Transform> getTransforms()
    {
        Set<Transform> result = new HashSet<Transform>();
        
        for( Logos logos : object2name.keySet() ) {
            if( logos instanceof Transform ) {
                result.add((Transform)logos);
            }
        }
        
        return result;
    }

    public Set<Form> getTopForms()
    {
        Set<Form> result = new HashSet<Form>();
        
        for( Logos logos : object2name.keySet() ) {
            if( logos instanceof Form ) {
                result.add((Form)logos);
            }
        }
        
        return result;
    }
    
    public Name getNameOf(Logos logos)
    {
    	return object2name.get(logos);
    }
        
    
    public void printToStream(Printable p)
    {
    	Iterator<Name> names_it = name2object.keySet().iterator();
    	while( names_it.hasNext() ) {
    	    Name name = names_it.next();
    	    Logos logos = get(name);
    	    logos.printToStream(name, p);
    	    p.println();
    	}
    }
    
}
