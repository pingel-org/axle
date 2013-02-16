package axle.quanta

import spire.math._
import axle.graph._
import math.Pi

class Angle extends Quantum {

  class AngleQuantity(
    magnitude: Number = one,
    _unit: Option[Q] = None,
    _name: Option[String] = None,
    _symbol: Option[String] = None,
    _link: Option[String] = None) extends Quantity(magnitude, _unit, _name, _symbol, _link)

  type Q = AngleQuantity

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): AngleQuantity =
    new AngleQuantity(one, None, name, symbol, link)

  def newQuantity(magnitude: Number, unit: AngleQuantity): AngleQuantity =
    new AngleQuantity(magnitude, Some(unit), None, None, None)

  def conversionGraph() = _conversionGraph

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Orders_of_magnitude_(length)"
  // "http://en.wikipedia.org/wiki/Distance"

  lazy val _conversionGraph = conversions(
    List(
      unit("degree", "°", Some("http://en.wikipedia.org/wiki/Degree_(angle)")),
      unit("radian", "rad", Some("http://en.wikipedia.org/wiki/Radian")),
      unit("circleDegrees", "circle", Some("http://en.wikipedia.org/wiki/Circle")),
      unit("circleRadians", "circle", Some("http://en.wikipedia.org/wiki/Circle"))
    ),
    (vs: Seq[Vertex[AngleQuantity]]) => vs match {
      case degree :: radian :: circleDegrees :: circleRadians :: Nil => trips2fns(List(
        (degree, circleDegrees, 360.0), // TODO: precision
        (radian, circleRadians, 2.0 * Pi),
        (circleDegrees, circleRadians, 1.0)
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
  
  lazy val clockwise90 = -90.0 *: °
  lazy val counterClockwise90 = 90.0 *: °
  
}

object Angle extends Angle()
