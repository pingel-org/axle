package org.pingel.gold;
import java.util.Set;
import java.util.TreeSet;

import org.pingel.util.Stringer;

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
    
    public String toString()
    {
        String result = "{";
        result += Stringer.render(sequences, ", ");
        result += "}";
        return result;
    }

}
