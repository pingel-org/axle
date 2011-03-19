package org.pingel.bayes

import org.pingel.util.DirectedGraph

class ModelGraph extends DirectedGraph[RandomVariable, ModelEdge]
{
  def name2variable = Map[String, RandomVariable]()
	
  def addVertex(variable: RandomVariable): RandomVariable = {
    super.addVertex(variable)
  }
	
  def getVariable(name: String): RandomVariable = name2variable(name)
  
}
