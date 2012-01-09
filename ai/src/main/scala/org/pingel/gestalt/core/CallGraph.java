package org.pingel.gestalt.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.pingel.gestalt.ui.Widget;
import org.pingel.util.DirectedGraph;
import org.pingel.util.Printable;
import org.pingel.util.PrintableStringBuffer;

public abstract class CallGraph
implements Widget
{
	public int id;
    public Transform transform;
    public CallVertex in;
    public TransformEdge macroEdge;
    
    protected CallVertex start;
    private int radius = 15;

    private boolean highlighted = false;

    private Point center = new Point(0, 0);
    
    private boolean unificationAttempted = false;
    private boolean unificationSucceeded = false;
    protected boolean hasNext = true;
    protected Form guard = null;
    protected Unifier unifier = new Unifier();
    protected List<CallVertex> outputs = new ArrayList<CallVertex>();
    private DirectedGraph<CallVertex, CallEdge> graph = new DirectedGraph<CallVertex, CallEdge>();
    
    public CallGraph(int id, History history, Lexicon lexicon, Transform transform, TransformEdge macro)
    {
    		this.id = id;
    		this.transform = transform;

        GLogger.global.entering("Call", "<init>");
        this.macroEdge = macro;
        history.addCall(this);
        guard = lexicon.getForm(transform.guardName).duplicate();
        guard.arrange(center);
    }

    public void unify(History history, CallVertex in)
    {
        this.in = in;

        start = graph.addVertex(new CallVertex(history.nextVertexId(), transform.start, in.getForm()));
        unificationSucceeded = guard.unify(guard.lambda, start.getForm(), unifier);
        if( ! unificationSucceeded ) {
            GLogger.global.fine("Call.constructor guard is not met");
            return;
        }
        
        unificationAttempted = true;
        hasNext = true;
    }

    public boolean isUnified()
    {
        return unificationSucceeded;
    }
    
    public DirectedGraph<CallVertex, CallEdge> getGraph()
    {
        return graph;
    }
    
	public int getId()
	{
		return this.id;
	}
	
	public CallVertex getStart()
	{
		return start;
	}
	
    public List<CallVertex> getOutputs()
    {
        return outputs;
    }
    
	public abstract double cost();
	
	public abstract void next(History history, Lexicon lexicon);
	
	public boolean hasNext() {
		return hasNext;
	}

    /* TODO deprecate this */
    
    public void proceed(History history, Lexicon lexicon) {
        while( hasNext ) {
            next(history, lexicon);
        }
    }
    
    private void printNetworkTo(CallVertex cv, Printable out, int i, int callId)
    {
        out.indent(i);
        cv.printTo(out);
        if( callId != -1 ) {
            out.print(" " + callId);
        }
        out.println();
        
        for( CallEdge outEdge : getGraph().outputEdgesOf(cv) ) {
            CallVertex successor = outEdge.getDest();
            printNetworkTo(successor, out, i+1, cv.getId());
        }
    }
    
	public void printNetworkTo(Printable out, int i)
	{
		out.indent(i);
		if( start == null ) {
			out.println("error");
		}
		else {
		    printNetworkTo(start, out, i+1, -1);
		}
	}
	
	public String toString()
	{
		Printable psb = new PrintableStringBuffer(new StringBuffer());
		
		printNetworkTo(psb, 0);
		
		return psb.toString();
	}

//    public void setHighlighted(boolean h)
//    {
//        highlighted = h;
//    }

//    public Form memberContains(Point p)
//    {
//        for( CallVertex cv : this.getGraph().getVertices() ) {
//            if( cv.getForm() != null ) {
//                Form f = cv.getForm().memberContains(p);
//                if( f != null ) {
//                    return f;
//                }
//            }
//        }
//        return null;
//    }
    
    private void _detachInput(History history, Lexicon lexicon, CallVertex cv)
    {
        Form f = cv.getForm();
        f.clearInputTo();
        cv.clearForm();
        lexicon.remove(f);

        for( CallVertex nextVertex : this.getGraph().getSuccessors(cv) ) {
            // TODO delete sub-graph
            _detachInput(history, lexicon, nextVertex);
        }

        CallGraph nextCG = f.getInputTo();
        if( nextCG != null ) {
            nextCG.detachInput(history, lexicon);
        }

    }
    
    public void detachInput(History history, Lexicon lexicon)
    {
        unificationSucceeded = false;
        unificationAttempted = false;
        guard = null;
        unifier = new Unifier();
        outputs = new Vector<CallVertex>();
        graph = new DirectedGraph<CallVertex, CallEdge>();

        _detachInput(history, lexicon, in);
    }

//    public Point getEntryCenter()
//    {
//        return center;
//    }

    
    public void arrange(Point p)
    {
        if( start == null ) {
            return;
        }

        start.move(p);
        
        Point p2 = new Point(p.x, p.y);
        p2.translate(100, 0);

        // TODO this should recurse through the nodes and arrange them
        // according to some sane layout

        // arrange all TransformVertexes as large circles (or perhaps dots), and then all CallVertices within them
        
        // Should the TransformVerctices already be layed out before the unification attempt?
        
        // Why is the unification attempt so off/on?  I think that's OK given that "unification"
        // happens exactly when a form is presented to a Transform.  But there are perhaps other
        // ways of thinking about this.

        
        for( TransformVertex tv : this.transform.getGraph().getVertices() )
        {
        		// arrange these in a circle
        		// then iterate through each of the CallVertexes that reference this TransformVertex
        		// arrange them in a circle
        }

        for( CallVertex cv : graph.getVertices() ) {
            if( cv != start ) {
                cv.move(p2);
            }
            p2.translate(0, 50);
            // find the center of the corresponding TransformVertex and pick an angle at which to radiate
            // from the TransformVertex
        }

        for( TransformEdge te : this.transform.getGraph().getEdges() )
        {
        		te.updatePoly();
        }

        for( CallEdge ce : graph.getEdges() ) {
            ce.updatePoly();
        }

    }
    
    public void move(Point p)
    {
        guard.move(p);

        Point dp = new Point(p.x - center.x, p.y - center.y);
        center.move(p.x, p.y);

        for( CallVertex cv : graph.getVertices() ) {
            Point newPosition = new Point(cv.getForm().center);
            newPosition.translate(dp.x, dp.y);
            cv.move(newPosition);
        }
        
        for( CallEdge ce : graph.getEdges() ) {
            ce.updatePoly();
        }

    }

    protected int distanceSquared(Point p1, Point p2)
    {
        // TODO put this elsewhere
        return (p2.x - p1.x)*(p2.x - p1.x) + (p2.y - p1.y)*(p2.y - p1.y);
    }

    public boolean intersects(Form f)
    {
        return distanceSquared(center, f.center) < 4*radius*radius;
    }

    public boolean contains(Point p)
    {
    		return guard.contains(p);
    	
//        if( ! unificationAttempted ) {
//        		System.out.println("CallGraph.contains: no unification attempted.  deferring to guard.contains");
//        		return guard.contains(p);
//        }
//        
//        for( CallVertex cv : getGraph().getVertices() ) {
//        		if( cv.getForm().contains(p) ) {
//        			System.out.println("CallGraph.contains: a vertex contains the point");
//            		return true;
//        		}
//        }
//
//        for( CallEdge ce : getGraph().getEdges() ) {
//        		if( ce.contains(p) ) {
//        			System.out.println("CallGraph.contains: an edge contains the point");
//        			return true;
//        		}
//        }
//        
//        return false;
    }
        
    public void paint(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g;

        g2d.setColor(Color.ORANGE);
        int bigRadius = radius + 5;
        Ellipse2D circle = new Ellipse2D.Double(center.x - bigRadius, center.y - bigRadius, 2*bigRadius, 2*bigRadius);
        g2d.fill(circle);
        g2d.setColor(Color.BLACK);
        g2d.draw(circle);
        
        
        if( ! unificationAttempted ) {
            guard.paint(g);
        }
        
        for( CallEdge ce : graph.getEdges() ) {
//            System.out.println("CallGraph.paint calling CallEdge.paint");
            ce.paint(g);
        }
        
        for( CallVertex cv : graph.getVertices() ) {
            cv.paint(g);
        }
        
    }

    public boolean mouseClicked(MouseEvent e, History history, Lexicon lexicon)
    {
        
        System.out.println("CallGraph.mouseClicked");
        
        if( contains(e.getPoint())) {
            
            if( e.getButton() == 2 ) {
                detachInput(history, lexicon);
                history.remove(this);
                return true;
            }
        }
        
        return false;
    }
    
    public void drag(Point p, History history, Lexicon lexicon)
    {
        System.out.println("CallGraph.mouseDragged");
        
        move(p);
    }

    public Widget mousePressed(MouseEvent e, History history, Lexicon lookupLexicon, Lexicon newLexicon)
    {
//        for( CallEdge edge : graph.getEdges() ) {
//            Widget w = edge.mousePressed(e, history, lookupLexicon, newLexicon);
//            if( w != null ) {
//                return w;
//            }
//        }

        if( contains(e.getPoint())) {
            return this;
        }
    	
        return null;
    }
    
    public void release(Point p, History history, Lexicon lookupLexicon, Lexicon newLexicon)
    {
        System.out.println("CallGraph.mouseReleased");

//        for( CallGraph cgOther : history.getCalls() ) {
//            for( CallVertex out : cg.getOutputs() ) {
//                if( out.getForm().intersects(cg.getStart()) ) {
//                    CallVertex start = cgOther.getStart();
//                    if( start == null ) {
//                        System.err.println("start == null!!!");
//                    }
//                    start.setForm(out.getForm());
//                    cgOther.proceed(history, newLexicon);
//                    // TODO shirt-circuit this loop
//                }
//            }
//        }
//        
//        Point bounds = cg.getBounds();
//        renderer.checkBounds(new Dimension(bounds.x, bounds.y));
//        
//        return true;
    }

    public Point getCenter()
    {
        return center;
    }

    public void setHighlighted(boolean h)
    {
    }

    public Point getBounds()
    {
        return new Point(0,0); // TODO !!!
    }
    
    
}
