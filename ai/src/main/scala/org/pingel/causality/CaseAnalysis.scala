package org.pingel.causality

import org.pingel.bayes.Probability
import org.pingel.bayes.RandomVariable
import org.pingel.bayes.VariableNamer
import org.pingel.forms.Variable
import org.pingel.forms.math.Product
import org.pingel.forms.math.Sigma
import scala.collection._

object CaseAnalysis {

    
    def caseAnalyze(probability: Probability, rv: RandomVariable, namer: VariableNamer): Sigma = {
      
        val variable = rv.nextVariable(namer)
        
        val firstQuestion = probability.getQuestion() // Set[Variable]
        var firstGiven = probability.getGiven() // returns a copy // Set<Variable>
        firstGiven += variable
        val firstActions = probability.getActions() // Set<Variable>
        
        val first = new Probability(firstQuestion, firstGiven, firstActions) // Probability
        
        val secondQuestion = mutable.Set[Variable]()
        secondQuestion += variable
        val secondGiven = probability.getGiven() // returns a copy
        val secondActions = probability.getActions()
        
        val second = new Probability(secondQuestion, secondGiven, secondActions)

        new Sigma(List(variable), new Product(first, second))
        
    }
    
}
