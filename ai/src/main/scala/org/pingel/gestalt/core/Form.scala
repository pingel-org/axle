package org.pingel.gestalt.core

import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Point
import java.awt.event.MouseEvent
import java.awt.geom.Ellipse2D
import org.pingel.gestalt.ui.Widget
import org.pingel.ptype.PType
import org.pingel.util.Printable

case class Form(lambda: Lambda=new Lambda())
extends Logos
with Widget
with Comparable[Form]
{
	private Map<Transform, Type> transform2type = new HashMap<Transform, Type>()

	public void addTransform(Transform transform, Type type)
	{
		transform2type.put(transform, type);
	}

	public Type getType(Transform transform)
	{
		return transform2type.get(transform);
	}
	
	protected Lambda lambda = null;

	def getLambda() = lambda
	val center = new Point()
	var color = Color.WHITE

    private ComplexForm parent;

//    private CallVertex outputFrom = null;
    
    private CallGraph inputTo = null;

    protected boolean detachable = false;

    public abstract Integer size();
    
    public abstract boolean equals(Form other);

    public abstract boolean unify(Lambda freeLambda, Form target, Unifier unifier);
    
    public abstract Form duplicate();

    public abstract Form duplicateAndReplace(Map<Name, Form> replacements);

    public Form traverse(Traversal traversal) {
		if( traversal == null ) {
		    return this;
		}
		else {
		    return _traverse(traversal, 0);
		}
    }

    abstract Form _traverse(Traversal traversal, int i);

    public abstract Form _duplicateAndEmbed(Traversal traversal, int i, Form s);

    public Form duplicateAndEmbed(Traversal traversal, Form s)
    {
		if( traversal == null ) {
		    return s;
		}
		else {
		    return _duplicateAndEmbed(traversal, 0, s);
		}
    }

//  public String toString()
//  {
//      Printable psb = new PrintableStringBuffer(new StringBuffer());
//      this.printToStream(null, psb);
//      return psb.toString();
//  }

    public void printToStream(Name name, Printable out)
    {
    		if( name != null ) {
    			out.print("form " + name);
    			if( lambda != null ) {
    				out.print(" [");
    				Iterator<Name> it = lambda.getNames().iterator();
    				while( it.hasNext() ) {
    					out.print(it.next().toString());
    					if( it.hasNext() ) {
    						out.print(" ");
    					}
    				}
    				out.print("]");
    			}
    			out.print(" ");
    		}
    }
    
    public void setParent(ComplexForm parent)
    {
        this.parent = parent;
    }

    public ComplexForm getParent()
    {
        return parent;
    }
    
    public Point getCenter()
    {
        return center;
    }

    public void clearInputTo()
    {
        inputTo = null;
    }
    
    public CallGraph getInputTo()
    {
        return inputTo;
    }

    public void reAttachToCallGraph(History history, Lexicon lexicon)
    {
        System.out.println("Form.reAttachToCallGraph: begin");
        
        if( parent == null ) {
            System.out.println("Form.reAttachToCallGraph: parent == null");
            if( inputTo != null ) {
                System.out.println("Form.reAttachToCallGraph: inputTo != null");
                inputTo.detachInput(history, lexicon);
                CallVertex cv = new CallVertex(history.nextVertexId(), inputTo.transform.start, this);
                CallGraph cg = inputTo.transform.constructCall(history.nextCallId(), history, lexicon, null);
                cg.unify(history, cv);
                if( cg.isUnified() ) {
                    while( cg.hasNext() ) {
                        cg.next(history, lexicon);
                    }
                    inputTo = cg;
                }
                else {
                    cg.detachInput(history, lexicon);
                    history.remove(cg);
                    inputTo = null;
                }
            }
        }
        else {
            parent.reAttachToCallGraph(history, lexicon);
        }
    }
    
    public boolean contains(Point p)
    {
        System.out.println("Form.contains: p = " + p + ", center = " + center + ", radius = " + radius);
        return distanceSquared(center, p) <= radius*radius;
    }


    public boolean moveOK(Point p)
    {
        if( parent != null ) {
            if( p.y <= parent.center.y ) {
                return false;
            }
            if( this == parent.getLeft() ) {
                if( p.x >= parent.center.x) {
                    return false;
                }
            }
            else {
                if( p.x <= parent.center.x) {
                    return false;
                }
            }
        }
        return true;
    }

    abstract public Point getBounds();

    abstract public void arrange(Point p);

    public void move(Point p)
    {
        center.move(p.x, p.y);

        // TODO this must work differently !!!
        
        if( inputTo != null ) {
            for( CallEdge edge : inputTo.getGraph().getEdges() ) {
                edge.updatePoly();
            }
        }

    }

    public abstract Form memberIntersects(Form other);
    
    public abstract Form memberContains(Point p);
    
    public boolean intersects(Form other)
    {
        return distanceSquared(center, other.center) < 4*radius*radius;
    }

    public boolean intersects(CallVertex cv)
    {
        if( cv.getForm() == null ) {
            System.err.println("Form.intersects: cv.getForm == null");
            return false;
        }
        
        return distanceSquared(center, cv.getForm().getCenter()) < 4*radius*radius;
    }
    
    public void setDetachable(boolean b)
    {
        detachable = b;
    }
    
    public boolean isDetachable()
    {
        return detachable;
    }

    // Detach 'this' from parent, and replace with new Blank
    public void detachFromParent(History history, Lexicon lexicon)
    {
        if( parent == null ) {
            System.err.println("attempting to detach something that has no parent");
            System.exit(1);
        }

        Blank blank = new Blank();
        blank.arrange(center);
        
        if( this == parent.getLeft() ){
            parent.setLeft(blank);
        }
        else {
            parent.setRight(blank);
        }
        blank.setParent(parent);
        
        parent.reAttachToCallGraph(history, lexicon);
        
        parent = null;
        lexicon.put(new Name(), this);
    }

    public void paint(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g;

        if( highlighted ) {
            g2d.setColor(Color.YELLOW);
        }
        else {
            g2d.setColor(color);
        }
        
        Ellipse2D circle = new Ellipse2D.Double(center.x - radius, center.y - radius, 2*radius, 2*radius);
        g2d.fill(circle);
        g2d.setColor(Color.BLACK);
        g2d.draw(circle);
    }

    public boolean mouseClicked(MouseEvent e, History history, Lexicon lexicon)
    {

        System.out.println("FormController.mouseClicked");
        
        // Delete the selected form
        
        if( contains(e.getPoint()) && isDetachable() ) {
            if( e.getButton() == 2 ) {
                if( getParent() != null ) {
                    detachFromParent(history, lexicon);
                }
                lexicon.remove(this);
            }
            
            return true;
        }
            
        return false;
    }

    public void drag(Point p, History history, Lexicon lexicon)
    {

        System.out.println("FormController.mouseDragged");

        if( moveOK(p) ) {
            move(p);
        }
    }

    public Widget mousePressed(MouseEvent e, History history, Lexicon lookupLexicon, Lexicon newLexicon)
    {
        System.out.println("FormController.mousePressed");

        Point p = e.getPoint();
        
        Form f = memberContains(p);
        if( f != null ) {
            
            if( e.getButton() == 3 ) {
                if( f.isDetachable() ) {
                    Form parent = f.getParent();
                    if( parent != null ) {
                        f.detachFromParent(history, newLexicon);
                    }
                    else if ( f.getInputTo() != null ) {
                        // TODO smells bad
                        CallGraph cg = f.getInputTo();
                        cg.detachInput(history, newLexicon);
                    }
                }
            }
            
            return f;
        }
        
        return null;
    }

    def release(p: Point, history: History, lookupLexicon: Lexicon, newlexicon: Lexicon): Unit = {
      
    	println("FormController.mouseReleased")

        if( ! moveOK(p) ) {
            return
        }

        if( parent != null ) {
            return
        }
 
        for( other <- newlexicon.getTopForms() ) {
            if( other != this ) {
                val f = other.memberIntersects(this)
                if( f instanceof Blank ) {
                    val blank = f.asInstanceOf[Blank]
                    if ( intersects(blank) ) {
                        ComplexForm binaryParent = blank.getParent()
                        if( blank == binaryParent.getLeft() ){
                            binaryParent.setLeft(this)
                        }
                        else {
                            binaryParent.setRight(this)
                        }
                        setParent(blank.getParent())
                        blank.setParent(null)
                        reAttachToCallGraph(history, newlexicon)
                        return
                    }
                }
            }
        }
        
        for( cg <- history.getCalls() ) {
            
            if( parent == null && cg.intersects(this) && ! cg.isUnified()) {

                val cv = new CallVertex(history.nextVertexId(), cg.transform.start, this)
                println("calling unify on callGraphToAttachTo")
                cg.unify(history, cv)
                if( cg.isUnified() ) {
                    cg.proceed(history, lookupLexicon)
                    cg.arrange(center)
                }
            }
        }
    }

}
