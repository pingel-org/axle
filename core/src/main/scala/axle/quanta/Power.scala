package axle.quanta

import java.math.BigDecimal
import axle.graph.JungDirectedGraph._

class Power extends Quantum {

  type Q = PowerQuantity
  type UOM = PowerUnit

  class PowerUnit(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None)
    extends UnitOfMeasurementImpl(name, symbol, link)

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): PowerUnit = new PowerUnit(name, symbol, link)

  class PowerQuantity(magnitude: BigDecimal, unit: PowerUnit) extends QuantityImpl(magnitude, unit)

  def newQuantity(magnitude: BigDecimal, unit: PowerUnit): PowerQuantity = new PowerQuantity(magnitude, unit)

  def conversionGraph() = _conversionGraph

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Power_(physics)"

  lazy val _conversionGraph = JungDirectedGraph[PowerUnit, BigDecimal](
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
    (vs: Seq[JungDirectedGraphVertex[PowerUnit]]) => vs match {
      case w :: kw :: mw :: gw :: miw :: hp :: lightBulb :: hooverDam :: mustangGT :: Nil => List(
        (w, kw, "1E3"),
        (kw, mw, "1E3"),
        (mw, gw, "1E3"),
        (miw, w, "1E3"),
        (w, lightBulb, 60),
        (mw, hooverDam, 2080),
        (hp, mustangGT, 420)
      )
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
