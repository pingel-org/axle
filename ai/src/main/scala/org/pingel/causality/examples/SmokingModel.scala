package org.pingel.causality.examples

import org.pingel.causality.CausalModel
import org.pingel.forms.Basic.PFunction
import org.pingel.bayes.ModelVisualizer
import org.pingel.bayes.Probability
import org.pingel.bayes.RandomVariable
import org.pingel.bayes.VariableNamer
import org.pingel.causality.docalculus.ActionToObservation
import org.pingel.causality.docalculus.DeleteAction
import org.pingel.causality.docalculus.ObservationToAction
import org.pingel.forms.Variable
import org.pingel.forms.Math.Product
import org.pingel.forms.Math.Sigma
import org.pingel.gestalt.core.Form

object SmokingModel extends CausalModel("Smoking Model") {

        val U = new RandomVariable("U", None, false)
        addVariable(U)
        
        val X = new RandomVariable("X") // smoke
        addVariable(X)
        addFunction(new PFunction(X, List(U)))

        val Z = new RandomVariable("Z") // tar
        addVariable(Z)
        addFunction(new PFunction(Z, List(X)))

        val Y = new RandomVariable("Y") // cancer
        addVariable(Y)
        addFunction(new PFunction(Y, List(Z, U)))

    def doTask1(model: CausalModel, namer: VariableNamer) = {
          
        var question = Set[Variable]()
        question += model.getVariable("Z").nextVariable(namer)
        
        val given = Set[Variable]()
        
        var actions = Set[Variable]()
        actions += model.getVariable("X").nextVariable(namer)
        
        val task1 = new Probability(question, given, actions)
        println("task1: " + task1.toString())

        for( q <- new ActionToObservation().apply(task1, model, namer) ) {
          println("after rule 2 application: " + q)
        }

    }

    def doTask2(model: CausalModel, namer: VariableNamer) = {
      
        var question = Set[Variable]()
        question += model.getVariable("Y").nextVariable(namer)
        
        val given = Set[Variable]()
        
        var actions = Set[Variable]()
        actions += model.getVariable("Z").nextVariable(namer)
        
        val task2 = new Probability(question, given, actions)
        println("task2: " + task2.toString())

        println("Trying ActionToObservation")
        val result = new ActionToObservation().apply(task2, model, namer)
        result.map( q => {
          println("after rule 2 application: " + q)
        })
        
        val e = task2.caseAnalysis(model.getVariable("X"), namer)
        println("after conditioning and summing over X:\n" + e)

        val p = e.getExpression() // asInstanceOf[Product]

        val former = p.getMultiplicand(0) // Probabiblity
        println("former = " + former)

        for( q <- (new ActionToObservation()).apply(former, model, namer) ) {
        	println("after rule ActionToObservation application: " + q)
        }

        val latter = p.getMultiplicand(1)
        println("latter = " + latter)

        for( q <- (new DeleteAction()).apply(latter, model, namer) ) {
        	println("after rule DeleteAction application: " + q)
        }
    }

    def doTask3(model: CausalModel, namer: VariableNamer) = {
      
        var question = Set[Variable](model.getVariable("Y").nextVariable(namer))

        val given = Set[Variable]()
        
        val actions = Set[Variable](model.getVariable("X").nextVariable(namer))

        val task3 = new Probability(question, given, actions)
        println("task3: " + task3.toString())
        
        val s = task3.caseAnalysis(model.getVariable("Z"), namer)
        println("after summing over Z:")
        println(s)
        
        val p = s.getExpression() // Product

        val former = p.getMultiplicand(0) // Probabiblity
        println("former = " + former)

        val result2 = new ObservationToAction().apply(former, model, namer)
        for( q <- result2 ) {
          println("after rule ObservationToAction application: " + q)
        }

        val former2 = result2(0).asInstanceOf[Probability] // Probability
        println("former2 = " + former2)
        
        for( q <- new DeleteAction().apply(former2, model, namer) ) {
        	println("after rule DeleteAction application: " + q)
        }

        println("latter = " + p.getMultiplicand(1)) // Probabiblity
        // see task 1
    }

    def main(args: Array[String]) = {
      
        val model = SmokingModel

//    	doTask1(model)
//    	doTask2(model)
        doTask3(model, new VariableNamer())
        
        ModelVisualizer.draw(model)
    }

}
