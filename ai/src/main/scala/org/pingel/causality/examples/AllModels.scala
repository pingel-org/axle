
package org.pingel.causality.examples

import org.pingel.causality.CausalModel
import org.pingel.bayes.Probability
import org.pingel.bayes.VariableNamer
import org.pingel.forms.Variable

object AllModels {

    def getModels() = {
      
//      new SmokingModel() ::
//      new Homework4Model(5, 0.2) ::
//      new MidtermModel1() ::
//      new MidtermModel2() ::
        new Model3dot8a() ::
        new Model3dot8b() ::
        new Model3dot8c() ::
        new Model3dot8d() ::
        new Model3dot8e() ::
        new Model3dot8f() ::
        new Model3dot8g() ::
        new Model3dot9a() ::
        new Model3dot9b() ::
        new Model3dot9c() ::
        new Model3dot9d() ::
        new Model3dot9e() ::
        new Model3dot9f() ::
        new Model3dot9g() ::
        new Model3dot9h() ::
        Nil
    }

    def getStandardQuantity(m: CausalModel) = {
        val namer = new VariableNamer()
        var question = Set[Variable](m.getVariable("Y").nextVariable(namer))
        var given = Set[Variable]()
        var actions = Set[Variable](m.getVariable("X").nextVariable(namer))
        new Probability(question, given, actions)
    }
    
    def main(args: Array[String]) {
        for( model <- getModels() ) {
//            System.out.println("is Markovian? " + model.isMarkovian());
            Probability yGivenDoX = getStandardQuantity(model);
            println(model.getName() + " identifies " + yGivenDoX.toString() + "? " + model.identifies(yGivenDoX));
        }
    }
}
