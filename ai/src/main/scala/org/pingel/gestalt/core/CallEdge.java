package org.pingel.gestalt.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;

import org.pingel.gestalt.ui.Widget;
import org.pingel.util.DirectedGraphEdge;

public class CallEdge
extends DirectedGraphEdge<CallVertex>
implements Widget
{
	public TransformEdge transformEdge;

	private int id;
	
    private static int radius = 15;
    Polygon poly;
    
	public CallEdge(int id, CallVertex v1, CallVertex v2, TransformEdge transformEdge)
	{
		super(v1, v2);
		this.transformEdge = transformEdge;
        updatePoly();
	}
	
	public int getId()
	{
		return id;
	}
    
    public void arrange(Point p)
    {
        getSource().getForm().arrange(p);
        Point p2 = new Point(p);
        p2.translate(100, 0);
        getDest().getForm().arrange(p2);

        updatePoly();
        
    }
    
    public Point getBounds()
    {
        return poly.getBounds().getLocation(); // TODO this is not quite right
    }
    
    
    public boolean contains(Point p)
    {
        return poly.contains(p);
    }
    
    public void updatePoly()
    {
        // System.out.println("CallEdge.updatePoly: center = " + getSource().getForm().center + ", radius = ");
        
        poly = new Polygon();
        poly.addPoint(getSource().getForm().center.x, getSource().getForm().center.y - radius);
        poly.addPoint(getSource().getForm().center.x, getSource().getForm().center.y + radius);
        poly.addPoint(getDest().getForm().center.x, getDest().getForm().center.y);
    }

    Color navajoWhite = new Color(255, 222, 173); // Navajo White

    public void paint(Graphics g) {
        
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(navajoWhite);
        g2d.fill(poly);
        g2d.setColor(Color.BLACK);
        g2d.draw(poly);
    }

    public void drag(Point p, History history, Lexicon lexicon) {

        System.out.println("CallEdge.mouseDragged");

        Point origin = getCenter();
        Point dp = new Point(p.x - origin.x, p.y - origin.y);

        getSource().move(p);
        
        Point newDestPosition = new Point(getDest().getCenter());
        newDestPosition.translate(dp.x, dp.y);
        getDest().move(newDestPosition);
        
        updatePoly();
    }

    public Widget mousePressed(MouseEvent e, History history, Lexicon lookupLexicon, Lexicon newLexicon) {

        System.out.println("CallEdge.mousePressed");

        Point p = e.getPoint();
        
        if( contains(p) ) {
            return this;
        }
        
        // TODO carry just a vertex
        // TODO see if a edge contains p
        // TODO see if the start vertex contains p
        
        return null;
    }

    public boolean mouseClicked(MouseEvent e, History history, Lexicon lexicon)
    {
        return false;
    }
    
    public void release(Point p, History history, Lexicon lookupLexicon, Lexicon newLexicon)
    {
    }

    public Point getCenter()
    {
        return getSource().getCenter();
    }

    public void setHighlighted(boolean h)
    {
    }

}
