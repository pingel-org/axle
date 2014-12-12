package axle.quanta

import scala.math.{ Pi => π }

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case object Angle extends Quantum {

  type Q = Angle.type

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Degree_(angle)"

  def unit[N](name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement[Q, N](name, symbol, wiki)

  def degree[N]: UnitOfMeasurement[Q, N] = unit("degree", "°", Some("http://en.wikipedia.org/wiki/Degree_(angle)"))
  def °[N] = degree[N]
  def radian[N]: UnitOfMeasurement[Q, N] = unit("radian", "rad", Some("http://en.wikipedia.org/wiki/Radian"))
  def rad[N] = radian[N]
  def circleDegrees[N]: UnitOfMeasurement[Q, N] = unit("circleDegrees", "circle", Some("http://en.wikipedia.org/wiki/Circle"))
  def circleRadians[N]: UnitOfMeasurement[Q, N] = unit("circleRadians", "circle", Some("http://en.wikipedia.org/wiki/Circle"))

  //  def clockwise90[N: Field: Eq] = -90 *: °[N]
  //  def counterClockwise90[N: Field: Eq] = 90 *: °[N]

  def units[N]: List[UnitOfMeasurement[Q, N]] =
    List(degree, radian, circleDegrees, circleRadians)

  def links[N: Field]: Seq[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])] =
    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])](
      (degree, circleDegrees, ScaleInt(360)),
      (radian, circleRadians, ScaleDouble(2 * π)),
      (circleDegrees, circleRadians, BijectiveIdentity[N]))

  //  implicit def conversionGraph[N: Field: Eq, DG[_, _]: DirectedGraph] =
  //    cgn(units[N], links)

}
