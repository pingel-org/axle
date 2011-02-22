
package org.pingel.causality.examples;

import org.pingel.bayes.CausalModel;
import org.pingel.bayes.Function;
import org.pingel.bayes.ModelVisualizer;
import org.pingel.bayes.RandomVariable;

public class Model3dot8a extends CausalModel
{

    public Model3dot8a()
    {
        super("3.8a");

        RandomVariable X = addVariable(new RandomVariable("X", "x"));
        RandomVariable Y = addVariable(new RandomVariable("Y", "y"));

        addFunction(new Function(Y, X));
    }
    
    public static void main(String[] argv)
    {
        CausalModel model = new Model3dot8a();
        ModelVisualizer.draw(model);
    }
    
    
}
