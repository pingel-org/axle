/*
 * Created on Jun 7, 2005
 *
 */
package org.pingel.causality.examples;

import org.pingel.bayes.CausalModel;
import org.pingel.bayes.Function;
import org.pingel.bayes.ModelVisualizer;
import org.pingel.bayes.RandomVariable;

public class Model3dot8e extends CausalModel {

    public Model3dot8e()
    {
        super("3.8e");

        RandomVariable X = addVariable(new RandomVariable("X", "x"));
        RandomVariable Y = addVariable(new RandomVariable("Y", "y"));
        RandomVariable Z = addVariable(new RandomVariable("Z", "z"));
        RandomVariable U = addVariable(new RandomVariable("U", "u", false));

        addFunction(new Function(X, U));
        addFunction(new Function(Y, Z, U));
        addFunction(new Function(Z, X));
    }
    
    public static void main(String[] argv)
    {
        CausalModel model = new Model3dot8e();
        ModelVisualizer.draw(model);
    }

}
