package org.pingel.angluin;
import java.util.Iterator;
import java.util.Vector;

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
        
        Iterator<Expression> i = iterator();
        while( i.hasNext() ) {
            
            Expression s = i.next();
            
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
        
        Iterator<Expression> i = iterator();
        while( i.hasNext() ) {
            
            Expression s = i.next();
            
            result += s.toString();
            
            if( i.hasNext() ) {
                result += ", ";
            }
        }
        
        result += ">";
        
        return result;
    }
    
}
