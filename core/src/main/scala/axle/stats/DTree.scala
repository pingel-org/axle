package axle.stats

import axle._
import collection._

class DTreeEdge {}

class DTreeNode {}

class DTree {

  def cluster(n: DTreeNode): Set[RandomVariable[_]] = ???

  def context(n: DTreeNode): Set[RandomVariable[_]] = ???

  def isLeaf(n: DTreeNode): Boolean = ???

  // returns an order pi with width(pi,G) no greater than the width
  // of dtree rooted at t

  def toEliminationOrder(t: DTreeNode): List[RandomVariable[_]] = {

    val result = mutable.ListBuffer[RandomVariable[_]]()

    if (isLeaf(t)) {
      val ct = context(t) // Set<RandomVariable>
      for (v <- cluster(t)) {
        if (!ct.contains(v)) {
          result += v
        }
      }
    } else {
      val leftPi: List[RandomVariable[_]] = ???
      val rightPi: List[RandomVariable[_]] = ???
      // TODO merge them
      // TODO add cluster(t) - context(t) in any order to result
    }
    result.toList
  }

}
