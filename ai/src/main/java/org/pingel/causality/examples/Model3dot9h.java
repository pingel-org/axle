
package org.pingel.causality.examples;

import org.pingel.bayes.CausalModel;
import org.pingel.bayes.Function;
import org.pingel.bayes.ModelVisualizer;
import org.pingel.bayes.RandomVariable;

public class Model3dot9h extends CausalModel {

    public Model3dot9h()
    {
        super("3.9h");

        RandomVariable W = addVariable(new RandomVariable("W", "w"));
        RandomVariable X = addVariable(new RandomVariable("X", "x"));
		RandomVariable Y = addVariable(new RandomVariable("Y", "y"));
		RandomVariable Z = addVariable(new RandomVariable("Z", "z"));
		RandomVariable U1 = addVariable(new RandomVariable("U1", "u1", false));
		RandomVariable U2 = addVariable(new RandomVariable("U2", "u2", false));
		RandomVariable U3 = addVariable(new RandomVariable("U3", "u3", false));
		RandomVariable U4 = addVariable(new RandomVariable("U4", "u4", false));

		addFunction(new Function(W, X, U3));
		addFunction(new Function(X, Z, U1, U2));
		addFunction(new Function(Y, W, U2, U4));
		addFunction(new Function(Z, U1, U3, U4));

    }

    public static void main(String[] argv)
    {
        CausalModel model = new Model3dot9h();
        ModelVisualizer.draw(model);
    }

}
