package axle.stats

import collection._

/**
 *
 * Read: "X is independent of Y given Z"
 */

case class Independence(
  X: immutable.Set[RandomVariable[_]],
  Z: immutable.Set[RandomVariable[_]],
  Y: immutable.Set[RandomVariable[_]]) {

  def variablesToString(s: Set[RandomVariable[_]]): String = "{" + (for (v <- s) yield v.getName()).mkString(", ") + "}"

  override def toString(): String =
    "I(" + variablesToString(X) + ", " + variablesToString(Z) + ", " + variablesToString(Y) + ")"

}
