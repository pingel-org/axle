
package org.pingel.gestalt.core;

import java.awt.Graphics;
import java.awt.Point;

import org.pingel.util.Printable;

public abstract class Logos {

    protected int radius = 15;

    protected boolean highlighted = false;
    
    public void setHighlighted(boolean h)
    {
        highlighted = h;
    }
    
    abstract public boolean contains(Point p);

    abstract public void paint(Graphics g);

    protected int distanceSquared(Point p1, Point p2)
    {
        return (p2.x - p1.x)*(p2.x - p1.x) + (p2.y - p1.y)*(p2.y - p1.y);
    }

    public abstract void printToStream(Name name, Printable p);

}
