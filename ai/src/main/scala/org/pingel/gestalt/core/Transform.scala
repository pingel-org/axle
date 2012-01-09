package org.pingel.gestalt.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.util.HashSet;
import java.util.Set;

import org.pingel.util.LabelledDirectedGraph;

public abstract class Transform extends Logos
{
    public Name guardName;
    public TransformVertex start;
    private Set<TransformVertex> exits = new HashSet<TransformVertex>();

    class TransformGraph extends LabelledDirectedGraph<TransformVertex, TransformEdge>
    {
        public TransformVertex addVertex(TransformVertex tv)
        {
            super.addVertex(tv);
            
            if( tv.isStart() ) {
                start = tv;
            }
            if( tv.isExit() ) {
                exits.add(tv);
            }
            return tv;
        }
    }

    private TransformGraph graph = new TransformGraph();
    
    Point center = new Point();
    
    Transform(Name guardName)
	{
    	this.guardName = guardName;
	}

    public LabelledDirectedGraph<TransformVertex, TransformEdge> getGraph()
    {
        return graph;
    }
    
    public abstract CallGraph constructCall(int id, History history, Lexicon lexicon, TransformEdge macro);

    public void arrange(Point p)
    {
        center.move(p.x, p.y);
    }
    
    public Point getCenter()
    {
        return center;
    }

    public void paint(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(Color.RED);
        Ellipse2D circle = new Ellipse2D.Double(center.x - radius, center.y - radius, 2*radius, 2*radius);
        g2d.fill(circle);
        g2d.setColor(Color.BLACK);
        g2d.draw(circle);
    }

    public void move(Point p)
    {
        center.move(p.x, p.y);
    }
    
    public boolean contains(Point p)
    {
        return distanceSquared(center, p) < radius*radius;
    }

}
