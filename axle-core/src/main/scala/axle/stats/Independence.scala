package axle.stats

import spire.algebra._

/**
 *
 * Read: "X is independent of Y given Z"
 */

case class Independence[T: Eq, N: Field](
  X: Set[RandomVariable[T, N]],
  Z: Set[RandomVariable[T, N]],
  Y: Set[RandomVariable[T, N]]) {

  def variablesToString(s: Set[RandomVariable[T, N]]): String = "{" + (s map { _.name }).mkString(", ") + "}"

  override def toString: String =
    "I(" + variablesToString(X) + ", " + variablesToString(Z) + ", " + variablesToString(Y) + ")"

}
