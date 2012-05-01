
package org.pingel.causality.examples

import org.pingel.causality.CausalModel
import org.pingel.bayes.Probability
import org.pingel.bayes.VariableNamer
import org.pingel.forms.Variable

object AllModels {

  def getModels() = {

    SmokingModel ::
      new Homework4Model(5, 0.2) ::
      MidtermModel1 ::
      MidtermModel2 ::
      Model3dot8a ::
      Model3dot8b ::
      Model3dot8c ::
      Model3dot8d ::
      Model3dot8e ::
      Model3dot8f ::
      Model3dot8g ::
      Model3dot9a ::
      Model3dot9b ::
      Model3dot9c ::
      Model3dot9d ::
      Model3dot9e ::
      Model3dot9f ::
      Model3dot9g ::
      Model3dot9h ::
      Nil
  }

  def getStandardQuantity(m: CausalModel) = {
    val namer = new VariableNamer()
    val question = Set(m.getVariable("Y").nextVariable(namer))
    val given = Set[Variable]()
    val actions = Set(m.getVariable("X").nextVariable(namer))
    new Probability(question, given, actions)
  }

  def main(args: Array[String]) {
    for (model <- getModels()) {
      println("is Markovian? " + model.isMarkovian())
      val yGivenDoX: Probability = getStandardQuantity(model)
      println(model.getName() + " identifies " + yGivenDoX.toString() + "? " + model.identifies(yGivenDoX))
    }
  }
}
