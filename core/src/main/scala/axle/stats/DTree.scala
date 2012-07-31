package axle.stats

import collection._

class DTreeEdge {}

class DTreeNode {}

class DTree {

  def cluster(n: DTreeNode): Set[RandomVariable[_]] = null // TODO

  def context(n: DTreeNode): Set[RandomVariable[_]] = null // TODO

  def isLeaf(n: DTreeNode): Boolean = false // TODO

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
      val leftPi: List[RandomVariable[_]] = null // TODO
      val rightPi: List[RandomVariable[_]] = null // TODO
      // TODO merge them
      // TODO add cluster(t) - context(t) in any order to result
    }
    result.toList
  }

}
