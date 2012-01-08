
package org.pingel.bayes

//import scalala.tensor.mutable._
//import scalala.tensor.dense._
import scala.collection._
import org.pingel.util.Matrix

class PartiallyDirectedGraph(variables: List[RandomVariable]) {

  var variable2index = Map[RandomVariable, Integer]()

  var connect = Matrix.zeros[Boolean](variables.size, variables.size)
  var mark = Matrix.zeros[Boolean](variables.size, variables.size)
  var arrow = Matrix.zeros[Boolean](variables.size, variables.size)
  
  for( i <- 0 until variables.size ) {
    variable2index += variables(i) -> new Integer(i)
    for( j <- 0 until variables.size ) {
      connect.setValueAt(i, j, false)
      mark.setValueAt(i, j, false)
      arrow.setValueAt(i, j, false)
    }
  }

  def indexOf(v: RandomVariable): Integer = variable2index(v).intValue
	
  def connect(v1: RandomVariable, v2: RandomVariable): Unit = {
    val i1 = indexOf(v1)
    val i2 = indexOf(v2)
    connect.setValueAt(i1, i2, true)
    connect.setValueAt(i2, i1, true)
  }
  
  def areAdjacent(v1: RandomVariable, v2: RandomVariable): Boolean =
    connect.valueAt(indexOf(v1), indexOf(v2))

  def undirectedAdjacent(v1: RandomVariable, v2: RandomVariable): Boolean = {
    val i1 = indexOf(v1)
    val i2 = indexOf(v2)
    connect.valueAt(i1, i2) && (! arrow.valueAt(i1, i2)) && (! arrow.valueAt(i2, i1))
  }

  def mark(v1: RandomVariable, v2: RandomVariable): Unit = {
    val i1 = indexOf(v1)
    val i2 = indexOf(v2)
    mark.setValueAt(i1, i2, true)
    mark.setValueAt(i2, i1, true)
  }
    
  def orient(v1: RandomVariable, v2: RandomVariable): Unit = {
    // Note: we assume they are already adjacent without checking
    val i1 = indexOf(v1)
    val i2 = indexOf(v2)
    arrow.setValueAt(i1, i2, true)
  }

  def size: Int = TODO()
  
  // TODO: scala version should probably use Option[Boolean] instead of allowing null
  def links(v: RandomVariable, arrowIn: Option[Boolean], marked: Option[Boolean], arrowOut: Option[Boolean]): List[RandomVariable] = {

    var result = mutable.ListBuffer[RandomVariable]()
    	
    val i = indexOf(v)
    	
    for(j <- 0 until size) {

      val u = variables(j)
      var pass = connect.valueAt(i, j)
      
      if( pass && (arrowIn != None) ) {
    	  pass = ( arrowIn.get == arrow.valueAt(j, i) )
      }
      
      if( pass && (marked != None) ) {
    	pass = ( marked.get == mark.valueAt(i, j) )
      }
      
      if( pass && (arrowOut != None) ) {
    	  pass = ( arrowOut.get == arrow.valueAt(i, j) )
      }
      
      if( pass ) {
    	result += u
      }
    }
    
    result.toList
  }

  def markedPathExists(from: RandomVariable, target: RandomVariable): Boolean = {
    // this will not terminate if there are cycles

    var frontier = mutable.ListBuffer[RandomVariable]()
    frontier += from
    	
    while( frontier.size > 0 ) {
      val head = frontier(0)
      frontier.removeElementAt(0)
      if( head.equals(target) ) {
    	return true
      }
      var follow = links(head, None, Some(true), Some(true))
      frontier.appendAll(follow)
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
    for( i <- 0 until variables.size ) {
      for( j <- 0 until variables.size ) {
    	  if( connect.valueAt(i, j) ) {
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
    for( i <- 0 until variables.size ) {
      for( j <- 0 until variables.size ) {
    	  if( mark.valueAt(i, j) ) {
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
    for( i <- 0 until variables.size ) {
      for( j <- 0 until variables.size ) {
    	  if( arrow.valueAt(i, j) ) {
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
