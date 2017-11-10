package axle.pgm

import axle.stats.Variable
import cats.kernel.Eq
import spire.algebra.Field

case class DTreeEdge()

case class DTreeNode()

case class DTree[T: Eq, N: Field]() {

  def cluster(n: DTreeNode): Set[Variable[T]] = ???

  def context(n: DTreeNode): Set[Variable[T]] = ???

  def isLeaf(n: DTreeNode): Boolean = ???

  // returns an order pi with width(pi,G) no greater than the width
  // of dtree rooted at t

  def toEliminationOrder(t: DTreeNode): List[Variable[T]] =
    if (isLeaf(t)) {
      val ct = context(t) // Set<Distribution>
      cluster(t).filter(v => !ct.contains(v)).toList
    } else {
      // val leftPi: List[Distribution[T, N]] = ???
      // val rightPi: List[Distribution[T, N]] = ???
      // TODO merge them
      // TODO add cluster(t) - context(t) in any order to result
      ???
    }

}
