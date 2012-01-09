package org.pingel.gestalt.core;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Map;

import org.pingel.util.Printable;

public class SimpleForm extends Form
{
	public Name name;
	
    public SimpleForm(Name name)
    {
        super();
        this.name = name;
    }
    
	public SimpleForm(Name name, Lambda lambda)
	{
		super(lambda);
		GLogger.global.entering("SimpleSituation", "<init>: " + name);
		this.name = name;
	}
	
	public Integer size()
	{
		return 1;
	}

	public int compareTo(Form other)
	{
		if( other instanceof ComplexForm ) {
			return -1;
		}
		else if ( ! ( other instanceof SimpleForm ) ) {
			return -1; // or throw an "incomparable" exception ???
		}
		else {
			SimpleForm otherSimpleForm = (SimpleForm) other;

			return name.compareTo(otherSimpleForm.name);
		}
	}
	
	public boolean equals(Form other) {
		
		if( ! ( other instanceof SimpleForm ) ) {
			return false;
		}
		
		SimpleForm leaf_other = (SimpleForm) other;
		
		return name.equals(leaf_other.name);
	}
	
	public boolean unify(Lambda freeLambda, Form target, Unifier unifier)
	{
		// Note: It may be more natural to merge the role of the unifier
		// and the "free" set.  Perhaps "free" could be represented as the
		// keys in unifier.  And unbound free variables will map to null
		// until they are bound.
		
		GLogger.global.entering("SimpleForm", "unify: this = " +
				this.toString() + ", target = " + target.toString());

		if( freeLambda != null && freeLambda.contains(this.name) ) {
			GLogger.global.fine(name + " is free");
			Form already = unifier.get(name);
			if( already == null ) {
				GLogger.global.fine("binding " + name + " to " + target);
				unifier.put(name, target);
				return true;
			}
			else {
				GLogger.global.fine(name + " already bound to " + already);
				return target.equals(already);
			}
		}
		else {
			GLogger.global.fine(name + " is not free. ");
			boolean e = equals(target);
			GLogger.global.fine("match will return " + e);
			return e;
		}
	}
	
	public Form duplicate()
	{
		return new SimpleForm(name, lambda); // TODO clone lambda if not null
	}
	
	public Form duplicateAndReplace(Map<Name, Form> replacements)
	{
		Form replacement = replacements.get(name);
		
		// System.out.println("this.name = " + this.name + ", replacement = " + replacement);
		
		if( replacement != null ) {
			return replacement.duplicate();
		}
		else {
			return duplicate();
		}
		
	}
	
	Form _traverse(Traversal traversal, int i)
	{
		if( i == traversal.length() ) {
			return this;
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
		else {
			System.out.println("malformed traversal");
			System.exit(1);
			return null;
		}
	}

    public Form memberIntersects(Form other)
    {
        if( this.intersects(other) ) {
            return this;
        }
        else {
            return null;
        }
    }
    
    public Form memberContains(Point p)
    {
        if( this.contains(p) ) {
            return this;
        }
        else {
            return null;
        }
    }

    public void arrange(Point p)
    {
        move(p);
    }

    public Point getBounds()
    {
        return center;
    }

    public String toString()
    {
        return name.base;
    }
    
    public void printToStream(Name name, Printable out)
    {
    	super.printToStream(name, out);
    	out.print(this.name.toString());
    }
    
    private Font font = new Font("TimesRoman", Font.BOLD, 24);

    public void paint(Graphics g)
    {
        super.paint(g);
        g.setFont(font);
        g.drawString(name.base, center.x - 5, center.y + 10);
    }

}
