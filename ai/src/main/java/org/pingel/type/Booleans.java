package org.pingel.type;

import org.pingel.bayes.Domain;
import org.pingel.bayes.Value;

public class Booleans extends Domain {

    final public static Value tVal = new Value("true");
    final public static Value fVal = new Value("false");

    public Booleans()
    {
    		super();
    		this.addValue(tVal);
    		this.addValue(fVal);
    }
    
}
