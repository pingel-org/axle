package axle.stats

import spire.algebra.Eq
import spire.algebra.Field

/**
 *
 * Read: "X is independent of Y given Z"
 */

case class Independence[T: Eq, N: Field](
  X: Set[Distribution[T, N]],
  Z: Set[Distribution[T, N]],
  Y: Set[Distribution[T, N]]) {

  def variablesToString(s: Set[Distribution[T, N]]): String = "{" + (s map { _.name }).mkString(", ") + "}"

  override def toString: String =
    "I(" + variablesToString(X) + ", " + variablesToString(Z) + ", " + variablesToString(Y) + ")"

}
