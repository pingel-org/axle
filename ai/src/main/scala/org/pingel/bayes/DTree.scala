package org.pingel.bayes

import scala.collection._

class DTree {

  def cluster(n: DTreeNode): Set[RandomVariable] = null // TODO
	
  def context(n: DTreeNode): Set[RandomVariable] = null // TODO
	
  def isLeaf(n: DTreeNode): Boolean = false // TODO
	
  // returns an order pi with width(pi,G) no greater than the width
  // of dtree rooted at t
	
  def toEliminationOrder(t: DTreeNode): List[RandomVariable] = {

    var result = mutable.ListBuffer[RandomVariable]()

    if( isLeaf(t) ) {
      var ct = context(t) // Set<RandomVariable>
      for( v <- cluster(t)) {
    	  if( ! ct.contains(v) ) {
    		  result += v
    	  }
      }
    }
    else {
      var leftPi: List[RandomVariable] = null // TODO
      var rightPi: List[RandomVariable] = null // TODO
      // TODO merge them
      // TODO add cluster(t) - context(t) in any order to result
    }
    result.toList
  }
	
}
