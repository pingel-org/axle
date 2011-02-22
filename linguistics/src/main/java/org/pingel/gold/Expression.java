package org.pingel.gold;
import java.util.Vector;

import org.pingel.util.Stringer;

public class Expression
{
    private Vector<Morpheme> v = new Vector<Morpheme>();
    
    public Expression()
    {
    }

    public void addMorpheme(Morpheme m)
    {
        v.add(m);
    }
    
    public int length()
    {
        return v.size();
    }

    public String toString()
    {
        String result = "\"";
        result += Stringer.render(v, " ");
        result += "\"";
        return result;
    }

}
