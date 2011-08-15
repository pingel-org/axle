
package org.pingel.causality.examples;

import org.pingel.bayes.CausalModel;
import org.pingel.bayes.Function;
import org.pingel.bayes.ModelVisualizer;
import org.pingel.bayes.RandomVariable;

public class Model3dot8d extends CausalModel {

    public Model3dot8d()
    {
        super("3.8d");

        RandomVariable X = addVariable(new RandomVariable("X", "x"));
        RandomVariable Y = addVariable(new RandomVariable("Y", "y"));
        RandomVariable Z = addVariable(new RandomVariable("Z", "z"));
        RandomVariable U = addVariable(new RandomVariable("U", "u", false));

        addFunction(new Function(X, Z, U));
        addFunction(new Function(Y, X, Z));
        addFunction(new Function(Z, U));
    }
    
    public static void main(String[] argv)
    {
        CausalModel model = new Model3dot8d();
        ModelVisualizer.draw(model);
    }

}
