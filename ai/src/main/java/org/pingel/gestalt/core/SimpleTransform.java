package org.pingel.gestalt.core;

import java.awt.Point;
import java.util.Iterator;
import java.util.Map;

import org.pingel.util.Printable;

public class SimpleTransform extends Transform
{
	public Name outName;
	public Map<Name, Name> map;
	public double cost;
	public TransformVertex exitNode; // TODO sort of a trivial case of super.exits; maybe I should split them

    Point center = new Point(0, 0);
    
	public SimpleTransform(Name guardName, Name outName, Map<Name, Name> map, double cost)
	{
		super(guardName);

		getGraph().addVertex(new TransformVertex(new Name("in"), true, false));
		
		GLogger.global.entering("SimpleSystm", "<init>: in = " + guardName.toString() +
				", out = " + outName.toString() );
		
		this.outName = outName;
		this.map = map;
		this.cost = cost;
		
		exitNode = getGraph().addVertex(new TransformVertex(new Name("out"), false, true));
        
        getGraph().addEdge(new TransformEdge(new Name(), null, start, exitNode));
	}

    public SimpleTransform()
    {
        super(new Name()); // TODO this should be the name of the precondition form
    }

	public CallGraph constructCall(int id, History history, Lexicon lexicon, TransformEdge macro)
	{
		GLogger.global.entering("SimpleSystm", "constructCall");
		
		return new SimpleTransformCall(id, history, lexicon, this, macro);
	}
	
	public String toString()
	{
		// this may not be right... do we want the names of the guard
		// and the output, 
		// or do we want the structural description?? !!!
		
		String result = new String();
		
		result += guardName + " " + outName + " ";
		
		result += "{";
		
		Iterator<Name> it = map.keySet().iterator();
		while( it.hasNext() ) {
			Name from = it.next();
			Name to = map.get(from);
			result += from + "/" + to;
			if( it.hasNext() ) {
				result += " ";
			}
		}
		
		result += "}";
		
		return result;
	}
	
	public void printToStream(Name name, Printable p) {
		p.print("transform " + name.base + " " + this.toString());
		p.println();
	}

}
