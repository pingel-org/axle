package axle.quanta

import axle.algebra.Vertex
import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field
import spire.math.Rational
import spire.math.Real
import math.{ Pi => π }

class Angle[DG[_, _]: DirectedGraph] extends Quantum {

  def wikipediaUrl = "http://en.wikipedia.org/wiki/Degree_(angle)"

  type Q = this.type

  def units[N: Field: Eq] = List[UnitOfMeasurement[Q, N]](
    unit("degree", "°", Some("http://en.wikipedia.org/wiki/Degree_(angle)")),
    unit("radian", "rad", Some("http://en.wikipedia.org/wiki/Radian")),
    unit("circleDegrees", "circle", Some("http://en.wikipedia.org/wiki/Circle")),
    unit("circleRadians", "circle", Some("http://en.wikipedia.org/wiki/Circle")))

  def links[N: Field: Eq] = {
    implicit val baseCG = cgnDisconnected[N, DG]
    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])](
      (degree, circleDegrees, ScaleInt(360)),
      (radian, circleRadians, ScaleDouble(2 * π)),
      (circleDegrees, circleRadians, BijectiveIdentity[N]))
  }

  def degree[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "degree")
  def °[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "degree")
  def radian[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "radian")
  def rad[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "radian")
  def circleDegrees[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "circleDegrees")
  def circleRadians[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "circleRadians")

  //  def clockwise90[N: Field: Eq] = -90 *: °[N]

  //  def counterClockwise90[N: Field: Eq] = 90 *: °[N]

}

case class Angle3() extends Quantum3

object Angle3 extends Quantum3 {

  def unit[N: Field: Eq](name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement3[Angle3, N](name, symbol, wiki)

  def degree[N: Field: Eq] = unit("degree", "°", Some("http://en.wikipedia.org/wiki/Degree_(angle)"))
  def °[N: Field: Eq] = degree[N]
  def radian[N: Field: Eq] = unit("radian", "rad", Some("http://en.wikipedia.org/wiki/Radian"))
  def rad[N: Field: Eq] = radian[N]
  def circleDegrees[N: Field: Eq] = unit("circleDegrees", "circle", Some("http://en.wikipedia.org/wiki/Circle"))
  def circleRadians[N: Field: Eq] = unit("circleRadians", "circle", Some("http://en.wikipedia.org/wiki/Circle"))

  def units[N: Field: Eq]: List[UnitOfMeasurement3[Angle3, N]] =
    List(degree, radian, circleDegrees, circleRadians)

  def links[N: Field: Eq]: Seq[(UnitOfMeasurement3[Angle3, N], UnitOfMeasurement3[Angle3, N], Bijection[N, N])] =
    List[(UnitOfMeasurement3[Angle3, N], UnitOfMeasurement3[Angle3, N], Bijection[N, N])](
      (degree, circleDegrees, ScaleInt(360)),
      (radian, circleRadians, ScaleDouble(2 * π)),
      (circleDegrees, circleRadians, BijectiveIdentity[N]))

  implicit def conversionGraph[Q <: Quantum3, N: Field: Eq, DG[_, _]: DirectedGraph] =
    Quantum3.cgn(units, links)

}
