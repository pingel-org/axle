package org.pingel.gestalt.core;

import org.pingel.util.DirectedGraphVertex;

public class TransformVertex 
implements DirectedGraphVertex<TransformEdge>
{
    public Name name;
    private boolean isStart;
    private boolean isExit;
    
    public TransformVertex(Name name, boolean is, boolean ie)
    {
        this.name = name;
        this.isStart = is;
        this.isExit = ie;
    }
    
    public boolean isStart()
    {
    	return isStart;
    }

    public void isStart(boolean is)
    {
    	isStart = is;
    }
    
    public boolean isExit()
    {
        return isExit;
    }

    public void isExit(boolean ie)
    {
        this.isExit = ie;
    }

    public String toString()
    {
        return name.toString();
    }

    
}
