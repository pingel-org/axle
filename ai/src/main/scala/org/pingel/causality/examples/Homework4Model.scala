package org.pingel.causality.examples;

import org.pingel.bayes.Case
import org.pingel.causality.CausalModel
import org.pingel.bayes.Domain
import org.pingel.causality.Function
import org.pingel.bayes.ModelVisualizer
import org.pingel.bayes.RandomVariable
import org.pingel.causality.RandomBooleanFunction
import org.pingel.gestalt.core.Form
import org.pingel.ptype.Booleans

class XorOrFunction(variable: RandomVariable, in1: RandomVariable, in2: RandomVariable, in3: RandomVariable) extends Function(variable, List(in1, in2, in3))
{
    def compute(m: CausalModel, memo: Case) = {
        val val1 = new Boolean(memo.valueOf(in1).toString()).booleanValue()
        val val2 = new Boolean(memo.valueOf(in2).toString()).booleanValue()
        val val3 = new Boolean(memo.valueOf(in3).toString()).booleanValue()

        if( (val2 || val3) ^ val1 ) {
          Booleans.tVal
        }
        else {
          Booleans.fVal
        }
    }
        
}

class Homework4Model(k: Int, p: Double) extends CausalModel("Homework 4 Model") {
  
  val bools = Some(new Booleans())

  var oldE: Option[RandomVariable] = None
  var oldEp: Option[RandomVariable] = None
  var oldX: Option[RandomVariable] = None
  var oldY: Option[RandomVariable] = None
		
  for(i <- 0 to k) {
	  val ei = new RandomVariable("E" + i, bools, false)
      addVariable(ei)
	  addFunction(new RandomBooleanFunction(ei, p))

	  val epi = new RandomVariable("E'" + i, bools, false)
	  addVariable(epi)
	  addFunction(new RandomBooleanFunction(epi, p))
			
	  val xi = new RandomVariable("X" + i, bools)
	  addVariable(xi)
	  
	  if( i == 0 ) {
		  addFunction(new RandomBooleanFunction(xi, 0.25))
	  }
	  else {
		  addFunction(new XorOrFunction(xi, oldE, oldX, oldY))
	  }

	  val yi = new RandomVariable("Y" + i, bools)
	  addVariable(yi)
	  
	  if( i == 0 ) {
		  addFunction(new RandomBooleanFunction(yi, 0.25))
	  }
	  else {
		  addFunction(new XorOrFunction(yi, oldEp, oldX, oldY))
      }

	  oldE = Some(ei)
	  oldEp = Some(epi)
	  oldX = Some(xi)
	  oldY = Some(yi)
	}
  
}


object Homework4Model {

	def main(args: Array[String]) = {
        val hw4 = new Homework4Model(5, 0.2)
        ModelVisualizer.draw(hw4)
	}
  
}
