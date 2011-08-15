package org.pingel.causality

import org.pingel.bayes.Probability
import org.pingel.bayes.RandomVariable
import org.pingel.bayes.VariableNamer
import org.pingel.forms.Variable
import org.pingel.forms.math.Product
import org.pingel.forms.math.Sigma

object CaseAnalysis {

    
    def caseAnalyze(probability: Probability, rv: RandomVariable, namer: VariableNamer): Sigma = {
      
        val variable = rv.nextVariable(namer)
        
        val firstQuestion = probability.getQuestion() // Set[Variable]
        val firstGiven = probability.getGiven() // returns a copy // Set<Variable>
        firstGiven.add(variable)
        val firstActions = probability.getActions() // Set<Variable>
        
        val first = new Probability(firstQuestion, firstGiven, firstActions) // Probability
        
        val secondQuestion = Set[Variable]()
        secondQuestion.add(variable)
        val secondGiven = probability.getGiven() // returns a copy
        val secondActions = probability.getActions()
        
        val second = new Probability(secondQuestion, secondGiven, secondActions)
        
        var itVars = List[Variable]()
        itVars.add(variable)
        
        new Sigma(itVars, new Product(first, second))
        
    }
    
}
