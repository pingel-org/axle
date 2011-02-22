package org.pingel.angluin;
import java.util.Iterator;

public class Learner
{
    private Text T;
    private Iterator<Expression> iterator;
    
    public Learner(Text T)
    {
        this.T = T;
        iterator = T.iterator();
    }
    
    public Grammar processNextExpression()
    {
        Expression s = nextExpression();
        
        // default implementation never guesses a Grammar
        return null;
    }
    
    public Expression nextExpression()
    {
        return iterator.next();
    }
    
    public final boolean hasNextExpression()
    {
        return iterator.hasNext();
    }
    
}
