package axle.stats

import cats.kernel.Eq
import spire.algebra.Field

import cats.Show

/**
 *
 * Read: "X is independent of Y given Z"
 */

case class Independence[T: Eq, N: Field](
  X: Set[Distribution[T, N]],
  Z: Set[Distribution[T, N]],
  Y: Set[Distribution[T, N]])

object Independence {

  def variablesToString[T, N](s: Set[Distribution[T, N]]): String = "{" + (s map { _.name }).mkString(", ") + "}"

  implicit def showIndependence[T, N]: Show[Independence[T, N]] = new Show[Independence[T, N]] {
    def show(i: Independence[T, N]): String =
      "I(" + variablesToString(i.X) + ", " + variablesToString(i.Z) + ", " + variablesToString(i.Y) + ")"
  }

}
