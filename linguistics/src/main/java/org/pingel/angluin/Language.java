package org.pingel.angluin;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class Language
{
    private Set<Expression> sequences = new TreeSet<Expression>(new ExpressionComparator());
    
    Language()
    {
        
    }
    
    public void addExpression(Expression s)
    {
        sequences.add(s);
    }
    
    public boolean equals(Language other)
    {
        return sequences.equals(other.sequences);
        
    }
    
    public Language prefixes()
    {
        // TODO !!!
        return null;
    }
    
    public Language goodFinals(Expression w)
    {
        // TODO !!!
        return null;
    }
    
    public String toString()
    {
        String result = "{";
        
        Iterator<Expression> i = sequences.iterator();
        while( i.hasNext() ) {
            
            Expression s = i.next();
            
            result += s.toString();
            
            if( i.hasNext() ) {
                result += ", ";
            }
        }
        
        result += "}";
        
        return result;
    }
    
}
