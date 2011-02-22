package org.pingel.gold;
import java.util.Iterator;
import java.util.Vector;

import org.pingel.util.Stringer;

public class Text
{
    private Vector<Expression> v;
    
    Text()
    {
        this.v = new Vector<Expression>();
    }
    
    public void addExpression(Expression s)
    {
        v.add(s);
    }
    
    public int length()
    {
        return v.size();
    }
    
    public boolean isFor(Language L)
    {
        Language content = content();
        
        return content.equals(L);
    }
    
    public Language content()
    {
        Language L = new Language();
        
        for( Expression s : v ) {
            if( ! ( s instanceof Hatch ) ) {
                L.addExpression(s);
            }
        }
        
        return L;
    }
    
    public Iterator<Expression> iterator()
    {
        return v.iterator();
    }
    
    public String toString()
    {
        String result = "<";
        result += Stringer.render(v, ", ");
        result += ">";
        return result;
    }
    
}
