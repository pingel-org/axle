/*
 * Created on Jun 2, 2005
 *
 */
package org.pingel.forms;

import org.pingel.gestalt.core.Form;
import org.pingel.gestalt.core.Name;
import org.pingel.gestalt.core.SimpleForm;
import org.pingel.type.Reals;

public class DoubleValue  {

    public double val;

    public Form createDoubleValue(double val)
    {
        this.val = val;
        return new SimpleForm(new Name(val + ""));
    }
    
    public boolean equals(Reals other) {
    	
        if( other instanceof DoubleValue ) {
            DoubleValue dv = (DoubleValue) other;
            return (dv != null) && this.val == dv.val;
        }
        else {
            return false;
        }
    }

    public int compareTo(Form other)
    {
        if( other instanceof DoubleValue ) {
            DoubleValue dv = (DoubleValue) other;
            return new Double(val).compareTo(dv.val);
        } 
        else {
            return -1;
        }

    }

    public String toLaTeX()
    {
        return val + "";
    }
}
