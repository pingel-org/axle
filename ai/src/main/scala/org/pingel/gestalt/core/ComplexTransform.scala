package org.pingel.gestalt.core;

import java.util.Iterator;

import org.pingel.util.Printable;

public class ComplexTransform extends Transform
{

	public ComplexTransform(Name guardName)
	{
		super(guardName);
	}

	public CallGraph constructCall(int id, History history, Lexicon lexicon, TransformEdge macro)
	{
		GLogger.global.entering("ComplexTransform", "constructCall");
		
		return new ComplexTransformCall(id, history, lexicon, this, macro);
	}    
	
    public String toString()
    {
    	String result = new String();
    	
    	result += guardName;
    	result += " {\n";
    	
    	for( TransformEdge arc : getGraph().getEdges() ) {
    		result += "   " + arc.toString() + "\n";
    	}
    	
    	result += "} <";
    	
    	Iterator<TransformVertex> nit = getGraph().getVertices().iterator();
    	while( nit.hasNext() ) {
    		TransformVertex n = nit.next();
    		if( n.isExit() ) {
    			result += n.name;
    			if( nit.hasNext() ) {
    				result += " ";
    			}
    		}
    	}
    	
    	result += ">\n";
    	
    	return result;
    }
    
    public void printToStream(Name name, Printable p)
    {
    	p.print("transform " + name.base + " " + this.toString());
    	p.println();
    }
    
}
