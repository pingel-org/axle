package org.pingel.gestalt.core;

import org.pingel.util.LabelledDirectedEdge;

public class TransformEdge extends LabelledDirectedEdge<TransformVertex>
{
    public Name transformName;
    public Traversal traversal;
    
    public TransformEdge(Name transformName,
    		Traversal traversal,
			TransformVertex source,
			TransformVertex dest)
    {
        super(source, transformName.base, dest);
        this.transformName = transformName;
    	this.traversal = traversal;
    }

    
    public String toString() {
    	
    	if( traversal == null ) {
    		return "apply " + transformName + " " +
    		getSource().toString() + " " + getDest().toString();
    	}
    	else {
    		return "apply " + transformName + " " +
    		getSource().toString() + "." + traversal + " " + getDest().toString();
    	}
    }
}
