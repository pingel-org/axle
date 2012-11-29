package axle.quanta

import java.math.BigDecimal
import axle.graph.JungDirectedGraph._

class Power extends Quantum {

  class PowerQuantity(
    magnitude: BigDecimal = oneBD,
    _unit: Option[Q] = None,
    _name: Option[String] = None,
    _symbol: Option[String] = None,
    _link: Option[String] = None) extends Quantity(magnitude, _unit, _name, _symbol, _link)

  type Q = PowerQuantity

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): PowerQuantity =
    new PowerQuantity(oneBD, None, name, symbol, link)

  def newQuantity(magnitude: BigDecimal, unit: PowerQuantity): PowerQuantity =
    new PowerQuantity(magnitude, Some(unit), None, None, None)

  def conversionGraph() = _conversionGraph

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Power_(physics)"

  lazy val _conversionGraph = JungDirectedGraph[PowerQuantity, BigDecimal](
    List(
      unit("watt", "W"),
      unit("kilowatt", "KW"),
      unit("megawatt", "MW"),
      unit("gigawatt", "GW"),
      unit("milliwatt", "mW"),
      unit("horsepower", "hp"),
      unit("light bulb", "light bulb"),
      unit("Hoover Dam", "Hoover Dam", Some("http://en.wikipedia.org/wiki/Hoover_Dam")),
      unit("2012 Mustang GT", "2012 Mustang GT", Some("http://en.wikipedia.org/wiki/Ford_Mustang"))
    ),
    (vs: Seq[JungDirectedGraphVertex[PowerQuantity]]) => vs match {
      case w :: kw :: mw :: gw :: miw :: hp :: lightBulb :: hooverDam :: mustangGT :: Nil => withInverses(List(
        (w, kw, "1E3"),
        (kw, mw, "1E3"),
        (mw, gw, "1E3"),
        (miw, w, "1E3"),
        (w, lightBulb, 60),
        (mw, hooverDam, 2080),
        (hp, mustangGT, 420)
      ))
    }
  )

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
