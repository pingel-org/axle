package org.pingel.gestalt.core;

public class Name implements Comparable
{
    static int counter = 0;

    public String base;

    public Name()
    {
		base = "s" + Integer.toString(counter);
		counter++;
    }

    public Name(String n)
    {
		base = n;
    }
    
    public String toString() 
    {
		return base;
    }
    
    public int compareTo(Object obj)
    {
		Name other = (Name) obj;
		return base.compareTo(other.base);
    }

    public boolean equals(Object obj)
    {
		return base.equals(((Name)obj).base);
    }

}
