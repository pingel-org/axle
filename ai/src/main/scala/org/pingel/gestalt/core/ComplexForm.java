package org.pingel.gestalt.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Map;

import org.pingel.util.Printable;

public class ComplexForm extends Form
{
    private Form left = null;
    private Form right = null;

    private Integer size = null;

    public ComplexForm(Form l, Form r)
    {
        super();
        left = l;
        right = r;
    }

    public ComplexForm(Form l, Form r, Lambda lambda)
    {
        super(lambda);
		left = l;
		right = r;
    }

    public Form getLeft()
    {
		return left;
    }

    public Form getRight()
    {
		return right;
    }

    public void setLeft(Form left)
    {
        this.left = left;
    }

    public void setRight(Form right)
    {
        this.right = right;
    }
    
    public Integer size()
    {
        if( size == null ) {
            size = 1 + left.size() + right.size();
        }
        return size;
    }

    public int compareTo(Form other) {
    	
    		if( other instanceof SimpleForm ) {
    			return 1;
    		}
    		else if ( ! ( other instanceof ComplexForm ) ) {
    			return -1; // or throw an "incomparable" exception ???
    		}
    		else {
    			ComplexForm otherComplexForm = (ComplexForm) other;

    			int leftCompare = left.compareTo(otherComplexForm.left);
    			if( leftCompare != 0 ) {
    				return leftCompare;
    			}

    			return right.compareTo(otherComplexForm.right);
    		}
    }

    public boolean equals(Form other) {

	if( other == null ) {
	    return false;
	}
	
	if( this == other ) {
	    return true;
	}

	if( ! ( other instanceof ComplexForm ) ) {
	    return false;
	}
	
	ComplexForm complex_other = (ComplexForm) other;
	
	return ( left.equals(complex_other.getLeft()) &&
		 right.equals(complex_other.getRight()) );
	
    }

    public boolean unify(Lambda freeLambda, Form target, Unifier result)
    {
		GLogger.global.entering("ComplexSituation", "unify: this: " + this.toString() + ", unify: target = " +
					target.toString() + ", result = " + result.toString());

		if( target instanceof ComplexForm ) {
			
			if( lambda != null ) {
				freeLambda.addAll(lambda);
			}
		    ComplexForm complex_target = (ComplexForm) target;
		    return (left.unify(freeLambda, complex_target.getLeft(), result) &&
			    right.unify(freeLambda, complex_target.getRight(), result) );
		}
		else {
		    return false;
		}
    }

    public Form duplicate()
    {
        // TODO clone lambda if not null
        Form leftDup = left.duplicate();
        Form rightDup = right.duplicate();
        ComplexForm dup = new ComplexForm(leftDup, rightDup, lambda);
        leftDup.setParent(dup);
        rightDup.setParent(dup);
        return dup;
    }

    public Form duplicateAndReplace(Map<Name, Form> replacements)
    {
    		return new ComplexForm(left.duplicateAndReplace(replacements),
    				right.duplicateAndReplace(replacements), lambda); // TODO clone lambda if not null
    }
    
    Form _traverse(Traversal traversal, int i)
    {
    		if( i == traversal.length() ) {
    			return this;
    		}
    		else if( traversal.elementAt(i) == 'r' ) {
    			return right._traverse(traversal, i+1);
    		}
    		else if ( traversal.elementAt(i) == 'l' ) {
    			return left._traverse(traversal, i+1);
    		}
    		else {
    			System.out.println("malformed traversal");
    			System.exit(1);
    			return null;
    		}
    }
    
    public Form _duplicateAndEmbed(Traversal traversal, int i, Form s)
    {
    		if( i == traversal.length() ) {
    			return s;
    		}
    		else if ( traversal.elementAt(i) == 'r' ) {
    			return new ComplexForm(left.duplicate(), right._duplicateAndEmbed(traversal, i+1, s), lambda); // TODO clone lambda if not null
    		}
    		else if ( traversal.elementAt(i) == 'l' ) {
    			return new ComplexForm(left._duplicateAndEmbed(traversal, i+1, s), right.duplicate(), lambda); // TODO clone lambda if not null
    		}
    		else {
    			System.out.println("malformed traversal");
    			System.exit(1);
    			return null;
    		}
    }
    
    public void printToStream(Name name, Printable out)
    {
    		super.printToStream(name, out);
    		out.print("(");
    		if( left == null ) {
    			out.print("?");
    		}
    		else {
    			left.printToStream(null, out);
    		}
    		out.print(" ");
    		if( right == null ) {
    			out.print("?");
    		}
    		else {
    			right.printToStream(null, out);
    		}
    		out.print(")");
    	
    }


    public String toString()
    {
        return "(" + left.toString() + " " + right.toString() + ")";
    }

    public String getLabel()
    {
        return "";
    }
    
    public void arrange(Point p)
    {
        move(p);
        
        Point lp = new Point(p);
        lp.translate(-50, 50);
        left.arrange(lp);

        Point rp = new Point(p);
        rp.translate(50, 50);
        right.arrange(rp);
    }

    
    public void move(Point p)
    {
        // TODO the vector from parent to child won't change if the parent is the one that's doing the moving
        
        Point p2l = new Point(left.center.x - center.x, left.center.y - center.y);
        Point p2r = new Point(right.center.x - center.x, right.center.y - center.y);
        Point old2new = new Point(p.x - center.x, p.y - center.y);
        Point lc = new Point(center);
        lc.translate(p2l.x, p2l.y);
        lc.translate(old2new.x, old2new.y);
        Point rc = new Point(center);
        rc.translate(p2r.x, p2r.y);
        rc.translate(old2new.x, old2new.y);
        
        super.move(p);
        left.move(lc);
        right.move(rc);
    }
    
    public void setDetachable(boolean b)
    {
        super.setDetachable(b);
        left.setDetachable(b);
        right.setDetachable(b);
    }

    public void setHighlighted(boolean h)
    {
        left.setHighlighted(h);
        right.setHighlighted(h);
        highlighted = h;
    }

    protected int getLeftXOffset()
    {
        return left.center.x - center.x;
    }

    protected int getLeftYOffset()
    {
        return left.center.y - center.y;
    }

    protected int getRightXOffset()
    {
        return right.center.x - center.x;
    }

    protected int getRightYOffset()
    {
        return right.center.y - center.y;
    }

    public Form memberIntersects(Form other)
    {
        if( this.intersects(other) ) {
            return this;
        }
        else {
            if( center.y <= other.center.y ) {
                if( center.x > other.center.x ) {
                    return left.memberIntersects(other);
                }
                else {
                    return right.memberIntersects(other);
                }
            }
            else {
                return null;
            }
        }
    }
    
    public Form memberContains(Point p)
    {
        if( this.contains(p) ) {
            return this;
        }
        else {
            if( center.y <= p.y ) {
                if( center.x > p.x ) {
                    return left.memberContains(p);
                }
                else {
                    return right.memberContains(p);
                }
            }
            else {
                return null;
            }
        }
    }

    
    public Point getBounds()
    {
        Point leftBounds = left.getBounds();
        Point rightBounds = right.getBounds();
        return new Point(Math.max(leftBounds.x, rightBounds.x), Math.max(leftBounds.y, rightBounds.y));
    }

    
    public void paint(Graphics g)
    {
        g.setColor(Color.BLACK);
        g.drawLine(center.x, center.y, left.center.x, left.center.y);
        g.setColor(Color.BLACK);
        g.drawLine(center.x, center.y, right.center.x, right.center.y);
        super.paint(g);
        left.paint(g);
        right.paint(g);
    }

}
