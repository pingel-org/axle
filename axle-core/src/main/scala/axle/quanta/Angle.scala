package axle.quanta

import spire.algebra._
import spire.math._
import spire.implicits._
import axle.graph._
import math.{Pi => π}

abstract class Angle[N: Field: Order: Eq] extends Quantum[N] {

  class AngleQuantity(
    magnitude: N = field.one,
    _unit: Option[Q] = None,
    _name: Option[String] = None,
    _symbol: Option[String] = None,
    _link: Option[String] = None) extends Quantity(magnitude, _unit, _name, _symbol, _link)

  type Q = AngleQuantity

  implicit def eqTypeclass: Eq[Q] = new Eq[Q] {
    def eqv(x: Q, y: Q): Boolean =
      (x.magnitude === y.magnitude) &&
        ((x.unitOption.isDefined && y.unitOption.isDefined && (x.unitOption.get === y.unitOption.get)) ||
            (x.unitOption.isEmpty && y.unitOption.isEmpty && x.equals(y)))
  }

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): AngleQuantity =
    new AngleQuantity(field.one, None, name, symbol, link)

  def newQuantity(magnitude: N, unit: AngleQuantity): AngleQuantity =
    new AngleQuantity(magnitude, Some(unit), None, None, None)

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Degree_(angle)"

}

object Angle extends Angle[Rational] {
  
  lazy val _conversionGraph = conversions(
    List(
      unit("degree", "°", Some("http://en.wikipedia.org/wiki/Degree_(angle)")),
      unit("radian", "rad", Some("http://en.wikipedia.org/wiki/Radian")),
      unit("circleDegrees", "circle", Some("http://en.wikipedia.org/wiki/Circle")),
      unit("circleRadians", "circle", Some("http://en.wikipedia.org/wiki/Circle"))
    ),
    (vs: Seq[Vertex[AngleQuantity]]) => vs match {
      case degree :: radian :: circleDegrees :: circleRadians :: Nil => trips2fns(List(
        (degree, circleDegrees, 360), // TODO: precision
        (radian, circleRadians, 2 * π),
        (circleDegrees, circleRadians, 1)
      ))
      case _ => Nil
    }
  )

  lazy val radian = byName("radian")
  lazy val rad = radian
  lazy val degree = byName("degree")
  lazy val ° = degree
  lazy val circleDegrees = byName("circleDegrees")
  lazy val circleRadians = byName("circleRadians")

  lazy val clockwise90 = -90 *: °
  lazy val counterClockwise90 = 90 *: °

  def conversionGraph: DirectedGraph[Q, Rational => Rational] = _conversionGraph
  
}
