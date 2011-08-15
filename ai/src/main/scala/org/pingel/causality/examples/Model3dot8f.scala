/*
 * Created on Jun 7, 2005
 *
 */
package org.pingel.causality.examples;

import org.pingel.bayes.CausalModel;
import org.pingel.bayes.Function;
import org.pingel.bayes.ModelVisualizer;
import org.pingel.bayes.RandomVariable;

public class Model3dot8f extends CausalModel {

    public Model3dot8f()
    {
        super("3.8f");

        RandomVariable X = addVariable(new RandomVariable("X", "x"));
        RandomVariable Y = addVariable(new RandomVariable("Y", "y"));
        RandomVariable Z1 = addVariable(new RandomVariable("Z1", "z1"));
        RandomVariable Z2 = addVariable(new RandomVariable("Z2", "z2"));
        RandomVariable U1 = addVariable(new RandomVariable("U1", "u1", false));
        RandomVariable U2 = addVariable(new RandomVariable("U2", "u2", false));

        addFunction(new Function(X, U1));
        addFunction(new Function(Y, X, Z1, Z2, U2));
        addFunction(new Function(Z1, X, U2));
        addFunction(new Function(Z2, Z1, U1));
        
    }
    
    public static void main(String[] argv)
    {
        CausalModel model = new Model3dot8f();
        ModelVisualizer.draw(model);
    }

}
