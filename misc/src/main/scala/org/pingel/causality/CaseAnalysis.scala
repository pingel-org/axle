package org.pingel.causality

import org.pingel.bayes.Probability
import org.pingel.bayes.RandomVariable
import org.pingel.bayes.VariableNamer
import org.pingel.forms.Variable
import org.pingel.forms.Math.Product
import org.pingel.forms.Math.Sigma
import scala.collection._

object CaseAnalysis {

  def caseAnalyze(probability: Probability, rv: RandomVariable, namer: VariableNamer): Sigma = {

    val variable = rv.nextVariable(namer)

    val firstQuestion = probability.getQuestion
    val firstGiven = probability.getGiven + variable
    val firstActions = probability.getActions

    val first = new Probability(firstQuestion, firstGiven, firstActions)

    val secondQuestion = Set(variable)
    val secondGiven = probability.getGiven
    val secondActions = probability.getActions

    val second = new Probability(secondQuestion, secondGiven, secondActions)

    new Sigma(List(variable), new Product(first, second))

  }

}
