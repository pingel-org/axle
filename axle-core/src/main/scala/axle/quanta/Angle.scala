package axle.quanta

import axle.algebra.Vertex
import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field
import spire.math.Rational
import spire.math.Real
import math.{ Pi => π }

case class Angle() extends Quantum

object Angle extends Quantum {

  def wikipediaUrl = "http://en.wikipedia.org/wiki/Degree_(angle)"

  //  def clockwise90[N: Field: Eq] = -90 *: °[N]
  //  def counterClockwise90[N: Field: Eq] = 90 *: °[N]

  def unit[N: Field: Eq](name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement[Angle, N](name, symbol, wiki)

  def degree[N: Field: Eq] = unit("degree", "°", Some("http://en.wikipedia.org/wiki/Degree_(angle)"))
  def °[N: Field: Eq] = degree[N]
  def radian[N: Field: Eq] = unit("radian", "rad", Some("http://en.wikipedia.org/wiki/Radian"))
  def rad[N: Field: Eq] = radian[N]
  def circleDegrees[N: Field: Eq] = unit("circleDegrees", "circle", Some("http://en.wikipedia.org/wiki/Circle"))
  def circleRadians[N: Field: Eq] = unit("circleRadians", "circle", Some("http://en.wikipedia.org/wiki/Circle"))

  def units[N: Field: Eq]: List[UnitOfMeasurement[Angle, N]] =
    List(degree, radian, circleDegrees, circleRadians)

  def links[N: Field: Eq]: Seq[(UnitOfMeasurement[Angle, N], UnitOfMeasurement[Angle, N], Bijection[N, N])] =
    List[(UnitOfMeasurement[Angle, N], UnitOfMeasurement[Angle, N], Bijection[N, N])](
      (degree, circleDegrees, ScaleInt(360)),
      (radian, circleRadians, ScaleDouble(2 * π)),
      (circleDegrees, circleRadians, BijectiveIdentity[N]))

  implicit def conversionGraph[N: Field: Eq, DG[_, _]: DirectedGraph] =
    Quantum.cgn(units, links)

}
