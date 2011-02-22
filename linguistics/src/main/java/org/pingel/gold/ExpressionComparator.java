package org.pingel.gold;
import java.util.Comparator;

public class ExpressionComparator implements Comparator<Expression>
{

    public int compare(Expression e1, Expression e2)
    {
        return e1.toString().compareTo(e2.toString());
    }

}
