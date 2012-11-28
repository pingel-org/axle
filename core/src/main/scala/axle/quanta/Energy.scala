package axle.quanta

import java.math.BigDecimal
import axle.graph.JungDirectedGraph._

class Energy extends Quantum {

  type Q = EnergyQuantity
  type UOM = EnergyUnit

  class EnergyUnit(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None)
    extends UnitOfMeasurementImpl(name, symbol, link)
  
  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): EnergyUnit = new EnergyUnit(name, symbol, link)

  class EnergyQuantity(magnitude: BigDecimal, unit: EnergyUnit) extends QuantityImpl(magnitude, unit)

  def newQuantity(magnitude: BigDecimal, unit: EnergyUnit): EnergyQuantity = new EnergyQuantity(magnitude, unit)

  def conversionGraph() = _conversionGraph
  
  import Power.{ kilowatt }
  import Time.{ hour }

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Energy"

  lazy val _conversionGraph = JungDirectedGraph[EnergyUnit, BigDecimal](
    List(
    ),
    (vs: Seq[JungDirectedGraphVertex[EnergyUnit]]) => vs match {
      case Nil => List(
      )
    }
  )

  lazy val kwh = derive(kilowatt.by[Time.type, this.type](hour, this))
  lazy val joule = unit("joule", "J")
  lazy val kilojoule = joule kilo
  lazy val megajoule = joule mega
  lazy val ton = quantity("4.184", megajoule, Some("ton TNT"), Some("T"), Some("http://en.wikipedia.org/wiki/TNT_equivalent"))
  lazy val kiloton = ton kilo
  lazy val megaton = ton mega
  lazy val gigaton = ton giga
  lazy val castleBravo = quantity("15", megaton, Some("Castle Bravo Thermonuclear Bomb"), None, Some("http://en.wikipedia.org/wiki/Castle_Bravo"))

}

object Energy extends Energy()
