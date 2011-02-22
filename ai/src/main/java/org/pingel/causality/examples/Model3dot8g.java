/*
 * Created on Jun 7, 2005
 *
 */
package org.pingel.causality.examples;

import org.pingel.bayes.CausalModel;
import org.pingel.bayes.Function;
import org.pingel.bayes.ModelVisualizer;
import org.pingel.bayes.RandomVariable;

public class Model3dot8g extends CausalModel {

    public Model3dot8g()
    {
        super("3.8g");

        RandomVariable X = addVariable(new RandomVariable("X", "x"));
        RandomVariable Y = addVariable(new RandomVariable("Y", "y"));
        RandomVariable Z1 = addVariable(new RandomVariable("Z1", "z1"));
        RandomVariable Z2 = addVariable(new RandomVariable("Z2", "z2"));
        RandomVariable Z3 = addVariable(new RandomVariable("Z3", "z3"));
        RandomVariable U1 = addVariable(new RandomVariable("U1", "u1", false));
        RandomVariable U2 = addVariable(new RandomVariable("U2", "u2", false));
        RandomVariable U3 = addVariable(new RandomVariable("U3", "u3", false));
        RandomVariable U4 = addVariable(new RandomVariable("U4", "u4", false));

        addFunction(new Function(X, Z2, U1, U2, U3));
        addFunction(new Function(Y, Z1, Z3, U1, U4));
        addFunction(new Function(Z1, X, Z2));
        addFunction(new Function(Z2, U3, U4));
        addFunction(new Function(Z3, Z2, U2));
        
    }
    
    public static void main(String[] argv)
    {
        CausalModel model = new Model3dot8g();
        ModelVisualizer.draw(model);
    }

}
