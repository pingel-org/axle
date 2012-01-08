package org.pingel.bayes

import org.pingel.util.DirectedGraph
import org.pingel.util.Lister
import scala.collection._

object Direction {

  val UNKNOWN = 0
  val OUTWARD = -1
  val INWARD = 1

}

case class Model(name: String="no name") {
	
  var graph = new ModelGraph()
  var newVarIndex = 0
  var name2variable = Map[String, RandomVariable]()
	
  def getName(): String = name
	
  def copyTo(other: Model): Unit = {
    other.name = name
		
    for( variable <- variables ) {
      other.addVariable(variable)
    }
    
    for( edge <- graph.getEdges() ) {
      other.connect(edge.getSource(), edge.getDest())
    }
    
  }
	
  def getGraph(): DirectedGraph[RandomVariable, ModelEdge] = graph
	
  def connect(source: RandomVariable, dest: RandomVariable): Unit = {
    graph.addEdge(new ModelEdge(source, dest))
  }
	
  var variables = mutable.ListBuffer[RandomVariable]()
	
  def addVariable(variable: RandomVariable): RandomVariable = {
    variables += variable
    name2variable += variable.getName -> variable
    graph.addVertex(variable)
  }
	
  def getRandomVariables(): List[RandomVariable] = variables.toList
	
  def getVariable(name: String): RandomVariable = name2variable(name)

  def deleteVariable(variable: RandomVariable): Unit = {
    variables.remove(variable)
    graph.deleteVertex(variable)
  }
	
  def numVariables(): Int = variables.size
	
  def blocks(from: Set[RandomVariable], to: Set[RandomVariable], given: Set[RandomVariable]): Boolean = {
    val path = _findOpenPath(Map[RandomVariable, Set[RandomVariable]](), Direction.UNKNOWN, null, from, to, given)
    path == null
  }
	
//  var rvNameGetter = new Lister[RandomVariable, String]() {
//    def function(rv: RandomVariable): String = rv.getName
//  }
	
  def _findOpenPath(
    visited: Map[RandomVariable, Set[RandomVariable]],
    priorDirection: Integer,
    prior: RandomVariable,
    current: Set[RandomVariable],
    to: Set[RandomVariable],
    given: Set[RandomVariable]): List[RandomVariable] =
  {
		
	println("_fOP: " + priorDirection +
			", prior = " + "TODO" + // ((prior == null ) ? "null" : prior.name) +
			", current = " +  current.map( _.getName ).mkString(", ") + 
			", to = " + to.map( _.getName ).mkString(", ") +
			", evidence = " + given.map( _.getName ).mkString(", ") )
		
    val cachedOuts = visited(prior) // Set<RandomVariable>
    if( cachedOuts != null ) {
      current.removeAll(cachedOuts)
    }
		
    for( variable <- current ) {
			
      var openToVar = false
      var directionPriorToVar = Direction.UNKNOWN
      if( prior == null ) {
    	  openToVar = true
      }
      else {
    	  directionPriorToVar = Direction.OUTWARD
		  if( getGraph().precedes(variable, prior) ) {
			  directionPriorToVar = Direction.INWARD
		  }
	
    	  if( priorDirection != Direction.UNKNOWN ) {
    		  var priorGiven = given.contains(prior)
    		  openToVar = (
    				  priorDirection == Direction.INWARD  &&
    				  ! priorGiven &&
    				  directionPriorToVar == Direction.OUTWARD) ||
    				  (priorDirection == Direction.OUTWARD &&
    				  ! priorGiven && 
    				  directionPriorToVar == Direction.OUTWARD) ||
    				  (priorDirection == Direction.INWARD  &&
    				  graph.descendantsIntersectsSet(variable, given) &&
    				  directionPriorToVar == Direction.INWARD)
    	  }
    	  else {
    		  openToVar = true
    	  }
      }
      
      if( openToVar ) {
    	  if( to.contains(variable) ) {
    		  return List(variable)
    	  }
    	  var neighbors = graph.getNeighbors(variable) // Set<RandomVariable>
    	  neighbors -= prior
	
    	  var visitedCopy = mutable.Map[RandomVariable, Set[RandomVariable]]()
    	  visitedCopy.putAll(visited)
    	  var outs = visited.get(prior) // Set<RandomVariable>
    	  if( outs == null ) {
    		  outs = new mutable.Set[RandomVariable]()
    		  visitedCopy.put(prior, outs)
    	  }
    	  outs += variable
	
    	  var path = _findOpenPath(visitedCopy, -1 * directionPriorToVar, variable, neighbors, to, given);
    	  if( path != null ) {
    		  path.add(variable)
    		  return path
    	  }
      }
    }
    return null
  }
	
}
