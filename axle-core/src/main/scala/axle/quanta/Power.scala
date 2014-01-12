package axle.quanta

import spire.algebra._
import spire.math._
import spire.implicits._
import axle.graph._

class Power extends Quantum {

  class PowerQuantity(
    magnitude: Number = one,
    _unit: Option[Q] = None,
    _name: Option[String] = None,
    _symbol: Option[String] = None,
    _link: Option[String] = None) extends Quantity(magnitude, _unit, _name, _symbol, _link)

  type Q = PowerQuantity

  implicit def eqTypeclass: Eq[Q] = new Eq[Q] {
    def eqv(x: Q, y: Q): Boolean =
      (x.magnitude === y.magnitude) &&
        ((x.unitOption.isDefined && y.unitOption.isDefined && (x.unitOption.get === y.unitOption.get)) ||
          (x.unitOption.isEmpty && y.unitOption.isEmpty && x.equals(y)))
  }

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): PowerQuantity =
    new PowerQuantity(one, None, name, symbol, link)

  def newQuantity(magnitude: Number, unit: PowerQuantity): PowerQuantity =
    new PowerQuantity(magnitude, Some(unit), None, None, None)

  def conversionGraph: DirectedGraph[Q, Number => Number] = _conversionGraph

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Power_(physics)"

  lazy val _conversionGraph = conversions(
    List(
      unit("watt", "W"),
      unit("kilowatt", "KW"),
      unit("megawatt", "MW"),
      unit("gigawatt", "GW"),
      unit("milliwatt", "mW"),
      unit("horsepower", "hp"),
      unit("light bulb", "light bulb"),
      unit("Hoover Dam", "Hoover Dam", Some("http://en.wikipedia.org/wiki/Hoover_Dam")),
      unit("2012 Mustang GT", "2012 Mustang GT", Some("http://en.wikipedia.org/wiki/Ford_Mustang"))),
    (vs: Seq[Vertex[PowerQuantity]]) => vs match {
      case w :: kw :: mw :: gw :: miw :: hp :: lightBulb :: hooverDam :: mustangGT :: Nil => trips2fns(List(
        (w, kw, 1E3),
        (kw, mw, 1E3),
        (mw, gw, 1E3),
        (miw, w, 1E3),
        (w, lightBulb, 60),
        (mw, hooverDam, 2080),
        (hp, mustangGT, 420)))
      case _ => Nil
    })

  lazy val watt = byName("watt")
  lazy val kilowatt = byName("kilowatt")
  lazy val megawatt = byName("megawatt")
  lazy val gigawatt = byName("gigawatt")
  lazy val milliwatt = byName("milliwatt")
  lazy val horsepower = byName("horsepower")
  lazy val lightBulb = byName("light bulb")
  lazy val hooverDam = byName("Hoover Dam")
  lazy val mustangGT = byName("2012 Mustang GT")

}

object Power extends Power()
