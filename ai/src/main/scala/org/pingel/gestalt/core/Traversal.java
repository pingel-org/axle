package org.pingel.gestalt.core;

public class Traversal
{
    private String offset;
    private char[] chars;
    
    public Traversal(String n) 
    {
		offset = n;
		chars = offset.toCharArray();
    }

    public String toString()
    {
        return offset;
    }

    public Traversal copy()
    {
		return new Traversal(offset);
    }

    public void append(String tail)
    {
		offset += tail;
		chars = offset.toCharArray();
    }

    public int length()
    {
		return chars.length;
    }

    public char elementAt(int i)
    {
		return chars[i];
    }

}
