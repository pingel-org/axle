
package org.pingel.bayes

object Constants {
  // I have no idea what the point of these are
  val TRUE = new Boolean(true)
  val FALSE = new Boolean(false)
}

class PartiallyDirectedGraph(variables: List[RandomVariable]) {


  var variable2index = Map[RandomVariable, Integer]()
	
  var connect = new Matrix[Boolean](vars.size, vars.size)
  var mark = new Matrix[Boolean](vars.size, vars.size)
  var arrow = new Matrix[Boolean](vars.size, vars.size)
  
  for( i <- 0 to variables.size-1 ) {
    variable2index.put(variables(i), new Integer(i));
    for( j <- 0 to variables.size-1 ) {
      connect(i, j) = false
      mark(i, j) = false
      arrow(i, j) = false
    }
  }

  def indexOf(v: RandomVariable): Integer = variable2index(v).intValue
	
  def connect(v1: RandomVariable, v2: RandomVariable): Unit = {
    val i1 = indexOf(v1)
    val i2 = indexOf(v2)
    connect(i1, i2) = true
    connect(i2, i1) = true
  }
  
  def areAdjacent(v1: RandomVariable, v2: RandomVariable): Boolean = {
    connect(indexOf(v1), indexOf(v2))
  }

  def undirectedAdjacent(v1: RandomVariable, v2: RandomVariable): Boolean = {
    val i1 = indexOf(v1)
    val i2 = indexOf(v2)
    connect(i1, i2) && ! arrow(i1, i2) && ! arrow(i2, i1)
  }

  def mark(v1: RandomVariable, v2: RandomVariable): Unit = {
    val i1 = indexOf(v1)
    val i2 = indexOf(v2)
    mark(i1, i2) = true
    mark(i2, i1) = true
  }
    
  def orient(v1: RandomVariable, v2: RandomVariable): Unit = {
    // Note: we assume they are already adjacent without checking
    val i1 = indexOf(v1)
    val i2 = indexOf(v2)
    arrow(i1, i2) = true
  }

  // TODO: scala version should probably use Option[Boolean] instead of allowing null
  def links(v: RandomVariable, arrowIn: Boolean, marked: Boolean, arrowOut: Boolean): List[RandomVariable] = {

    var result = new List[RandomVariable]()
    	
    val i = indexOf(v)
    	
    for(j <- 0 to size-1) {

      val u = variables(j)
      var pass = connect(i, j)
      
      if( pass && (arrowIn != null) ) {
    	  pass = ( arrowIn.booleanValue() == arrow(j, i) )
      }
      
      if( pass && (marked != null) ) {
    	pass = ( marked.booleanValue() == mark(i, j) )
      }
      
      if( pass && (arrowOut != null) ) {
    	  pass = ( arrowOut.booleanValue() == arrow(i, j) )
      }
      
      if( pass ) {
    	result.add(u)
      }
    }
    
    result
  }

  def markedPathExists(from: RandomVariable, target: RandomVariable): Boolean = {
    // this will not terminate if there are cycles

    var frontier = List[RandomVariable]()
    frontier.add(from)
    	
    while( frontier.size > 0 ) {
      val head = frontier.firstElement()
      frontier.removeElementAt(0)
      if( head.equals(target) ) {
    	return true
      }
      var follow = links(head, null, Constants.TRUE, Constants.TRUE)
      frontier.addAll(follow)
    }
    false
  }
    
  override def toString(): String = {
        
    var result = ""
    
    for( rv <- variables ) {
      result += "var " + rv + " has index " + variable2index(rv)
      result += "\n"
    }
    
    result += "connect\n\n"
    for( i <- 0 to variables.size-1 ) {
      for( j <- 0 to variables.size-1 ) {
	if( connect(i, j) ) {
	  result += "x"
	}
	else {
	  result += " "
	}
      }
      result += "\n"
    }
    result += "\n\n"
    
    result += "mark\n\n"
    for( i <- 0 to variables.size-1 ) {
      for( j <- 0 to variables.size-1 ) {
	if( mark(i, j) ) {
	  result += "x"
	}
	else {
	  result += " "
	}
      }
      result += "\n"
    }
    result += "\n\n"
    
    result += "arrow\n\n"
    for( i <- 0 to variables.size-1 ) {
      for( j <- 0 to variables.size-1 ) {
	if( arrow(i, j) ) {
	  result += "x"
	}
	else {
	  result += " "
	}
      }
      result += "\n"
    }
    result += "\n\n"
    
    result
  }
}
