package org.pingel.bayes;

import org.pingel.util.DirectedGraph
import org.pingel.util.Lister

object Direction {

  val UNKNOWN = 0
  val OUTWARD = -1
  val INWARD = 1

}

class Model(name: String="no name") {
	
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
    graph.addEdge(new ModelEdge(source, dest));
  }
	
  var variables = List[RandomVariable]()
	
  def addVariable(variable: RandomVariable): RandomVariable = {
    variables.add(variable);
    name2variable += variable.getName -> variable
    graph.addVertex(variable)
  }
	
  def getRandomVariables(): List[RandomVariable] = variables
	
  def getVariable(name: String): RandomVariable = name2variable(name)

  def deleteVariable(variable: RandomVariable): Unit = {
    variables.remove(variable)
    graph.deleteVertex(variable)
  }
	
  def numVariables(): Integer = variables.size()
	
  def blocks(from: Set[RandomVariable], to: Set[RandomVariable], given: Set[RandomVariable]): Boolean = {
    val path = _findOpenPath(Map[RandomVariable, Set[RandomVariable]](), Direction.UNKNOWN, null, from, to, given)
    path == null
  }
	
  var rvNameGetter = new Lister[RandomVariable, String]() {
    def function(rv: RandomVariable): String = rv.getName
  }
	
  def _findOpenPath(
    visited: Map[RandomVariable, Set[RandomVariable]],
    priorDirection: Integer,
    prior: RandomVariable,
    current: Set[RandomVariable],
    to: Set[RandomVariable],
    given: Set[RandomVariable]): List[RandomVariable] =
  {
		
//		System.out.println("_fOP: " + priorDirection +
//		", prior = " + ((prior == null ) ? "null" : prior.name) +
//		", current = " + rvNameGetter.execute(current) +
//		", to = " + rvNameGetter.execute(to) +
//		", evidence = " + rvNameGetter.execute(given));
		
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
	  var path = List[RandomVariable]()
	  path.add(variable);
	  return path
	}
	var neighbors = graph.getNeighbors(variable) // Set<RandomVariable>
	neighbors.remove(prior)
	
	var visitedCopy = Map[RandomVariable, Set[RandomVariable]]()
	visitedCopy.putAll(visited)
	var outs = visited.get(prior) // Set<RandomVariable>
	if( outs == null ) {
	  outs = Set[RandomVariable]()
	  visitedCopy.put(prior, outs)
	}
	outs.add(variable)
	
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
