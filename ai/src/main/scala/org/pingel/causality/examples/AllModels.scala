
package org.pingel.causality.examples;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.pingel.bayes.CausalModel;
import org.pingel.bayes.Probability;
import org.pingel.bayes.VariableNamer;
import org.pingel.forms.Variable;

public class AllModels {

    public static List<CausalModel> getModels()
    {
        List<CausalModel> models = new Vector<CausalModel>();

//        models.add(new SmokingModel());
//        models.add(new Homework4Model(5, 0.2));
//        models.add(new MidtermModel1());
//        models.add(new MidtermModel2());
        models.add(new Model3dot8a());
        models.add(new Model3dot8b());
        models.add(new Model3dot8c());
        models.add(new Model3dot8d());
        models.add(new Model3dot8e());
        models.add(new Model3dot8f());
        models.add(new Model3dot8g());
        models.add(new Model3dot9a());
        models.add(new Model3dot9b());
        models.add(new Model3dot9c());
        models.add(new Model3dot9d());
        models.add(new Model3dot9e());
        models.add(new Model3dot9f());
        models.add(new Model3dot9g());
        models.add(new Model3dot9h());
        
        return models;
    }

    public static Probability getStandardQuantity(CausalModel m)
    {
        VariableNamer namer = new VariableNamer();
        
        Set<Variable> question = new HashSet<Variable>();
        question.add(m.getVariable("Y").nextVariable(namer));
        
        Set<Variable> given = new HashSet<Variable>();
        
        Set<Variable> actions = new HashSet<Variable>();
        actions.add(m.getVariable("X").nextVariable(namer));

        return new Probability(question, given, actions);
        
    }
    
    public static void main(String[] argv)
    {
        List<CausalModel> models = getModels();

        for( CausalModel model : models ) {
//            System.out.println("is Markovian? " + model.isMarkovian());
            Probability yGivenDoX = getStandardQuantity(model);
            System.out.println(model.getName() + " identifies " + yGivenDoX.toString() + "? " + model.identifies(yGivenDoX));
        }
    }
}
