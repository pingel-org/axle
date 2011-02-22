

package org.pingel.angluin;
public class Symbol
{
    private String s;
    
    Symbol(String s, Alphabet alphabet)
    {
        this.s = s;
        alphabet.addSymbol(this);
    }
    
    public String toString()
    {
        return s;
    }
    
    public boolean equals(Symbol other)
    {
        return s.equals(other.s);
    }
    
}
