package org.pingel.angluin;
import java.util.Comparator;

public class ExpressionComparator implements Comparator<Expression>
{
    
    public int compare(Expression o1, Expression o2)
    {
        return (o1.toString()).compareTo(o2.toString());
    }
    
}
