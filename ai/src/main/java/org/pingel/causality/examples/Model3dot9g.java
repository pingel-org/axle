
package org.pingel.causality.examples;

import org.pingel.bayes.CausalModel;
import org.pingel.bayes.Function;
import org.pingel.bayes.ModelVisualizer;
import org.pingel.bayes.RandomVariable;

public class Model3dot9g extends CausalModel {

    public Model3dot9g()
    {
        super("3.9g");

        RandomVariable X = addVariable(new RandomVariable("X", "x"));
		RandomVariable Y = addVariable(new RandomVariable("Y", "y"));
		RandomVariable Z1 = addVariable(new RandomVariable("Z1", "z1"));
		RandomVariable Z2 = addVariable(new RandomVariable("Z2", "z2"));
		RandomVariable U1 = addVariable(new RandomVariable("U1", "u1", false));
		RandomVariable U2 = addVariable(new RandomVariable("U2", "u2", false));
		
		addFunction(new Function(X, U1));
		addFunction(new Function(Z1, X, U2));
		addFunction(new Function(Z2, U1, U2));
		addFunction(new Function(Y, Z1, Z2));
		
    }

    public static void main(String[] argv)
    {
        CausalModel model = new Model3dot9g();
        ModelVisualizer.draw(model);
    }

}
