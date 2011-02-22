package org.pingel.gestalt.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;

import org.pingel.util.DirectedGraphVertex;
import org.pingel.util.Printable;

public class CallVertex
implements DirectedGraphVertex<CallEdge>
{
	private int id;
	private TransformVertex tv;
	private Form form;

    private Point center = new Point();
    
    private static int radius = 15;
    
	public CallVertex(int id, TransformVertex tv, Form form)
	{
		super();
		this.id = id;
		this.tv = tv;
		this.form = form;
	}

	public int getId()
	{
		return id;
	}

	public TransformVertex getTransformVertex()
	{
		return tv;
	}

    public void clearForm()
    {
        form = null;
    }

    public void setForm(Form f)
    {
        form = f;
    }

    public Point getCenter()
    {
        return center;
    }
    
    public void move(Point p)
    {
//        System.out.println("CallVertex.move: p = " + p);
        
        center.move(p.x, p.y);
        if( form != null ) {
            form.arrange(p);
        }
    }
    
	public Form getForm()
	{
		return form;
	}
	
    public void printTo(Printable out)
    {
        if( tv.isExit() ) {
            out.print("*");
        }
        else {
            out.print(" ");
        }
        out.print("<" + tv.name + ", " + getId() + ", ");
        form.printToStream(new Name("s" + id), out);
        out.print(">");
    }

    // See http://www.pitt.edu/~nisg/cis/web/cgi/rgb.html for colors
    Color darkOrange = new Color(255, 140, 0); // Dark orange 
    // 178, 34, 34); // firebrick
    Color turquoise = new Color(64, 224, 208); // turquose
    
    public void paint(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g;

        if( form == null ) {
            Ellipse2D inCircle = new Ellipse2D.Double(center.x - radius, center.y - radius, 2*radius, 2*radius);
            g2d.setColor(darkOrange);
            g2d.fill(inCircle);
            g2d.setColor(Color.BLACK);
            g2d.draw(inCircle);
        }
        else {
//            System.out.println("painting cv's form: " + getForm().toString());
            form.paint(g);
        }
        
    }
}
