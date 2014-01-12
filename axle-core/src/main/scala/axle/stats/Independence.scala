package axle.stats

import spire.algebra._

/**
 *
 * Read: "X is independent of Y given Z"
 */

case class Independence[T: Eq](
  X: Set[RandomVariable[T]],
  Z: Set[RandomVariable[T]],
  Y: Set[RandomVariable[T]]) {

  def variablesToString(s: Set[RandomVariable[T]]): String = "{" + (s map { _.name }).mkString(", ") + "}"

  override def toString(): String =
    "I(" + variablesToString(X) + ", " + variablesToString(Z) + ", " + variablesToString(Y) + ")"

}
