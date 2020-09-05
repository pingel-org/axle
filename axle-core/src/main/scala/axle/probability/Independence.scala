package axle.probability

import cats.Show

/**
 *
 * Read: "X is independent of Y given Z"
 */

case class Independence[T](
  X: Set[Variable[T]],
  Z: Set[Variable[T]],
  Y: Set[Variable[T]])

object Independence {

  def variablesToString[T, N](s: Set[Variable[T]]): String = "{" + (s map { _.name }).mkString(", ") + "}"

  implicit def showIndependence[T, N]: Show[Independence[T]] = i =>
    "I(" + variablesToString(i.X) + ", " + variablesToString(i.Z) + ", " + variablesToString(i.Y) + ")"

}
