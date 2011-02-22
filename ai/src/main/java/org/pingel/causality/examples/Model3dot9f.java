
package org.pingel.causality.examples;

import org.pingel.bayes.CausalModel;
import org.pingel.bayes.Function;
import org.pingel.bayes.ModelVisualizer;
import org.pingel.bayes.RandomVariable;

public class Model3dot9f extends CausalModel {

    public Model3dot9f()
    {
        super("3.9f");

        RandomVariable X = addVariable(new RandomVariable("X", "x"));
		RandomVariable Y = addVariable(new RandomVariable("Y", "y"));
		RandomVariable Z = addVariable(new RandomVariable("Z", "z"));
		RandomVariable U1 = addVariable(new RandomVariable("U1", "u1", false));
		RandomVariable U2 = addVariable(new RandomVariable("U2", "u2", false));
		
		addFunction(new Function(X, U1));
		addFunction(new Function(Z, X, U2));
		addFunction(new Function(Y, Z, U1, U2));
    }

    public static void main(String[] argv)
    {
        CausalModel model = new Model3dot9f();
        ModelVisualizer.draw(model);
    }

}
