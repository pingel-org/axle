package org.pingel.bayes

import org.pingel.util.DirectedGraph
import org.pingel.util.DirectedGraphEdge

class ModelGraph extends DirectedGraph[RandomVariable, ModelEdge]
{
  def name2variable = Map[String, RandomVariable]()
	
  def addVertex(variable: RandomVariable): RandomVariable = {
    super.addVertex(variable)
  }
	
  def getVariable(name: String): RandomVariable = name2variable(name)
  
}

class ModelEdge(v1: RandomVariable, v2: RandomVariable)
extends DirectedGraphEdge[RandomVariable]
{
  def getSource = v1
  def getDest = v2
}

