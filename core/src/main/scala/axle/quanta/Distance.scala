package axle.quanta

import java.math.BigDecimal
import axle.graph.JungDirectedGraph._

class Distance extends Quantum {

  type Q = DistanceQuantity
  type UOM = DistanceUnit

  class DistanceUnit(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None)
    extends UnitOfMeasurementImpl(name, symbol, link)

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): DistanceUnit = new DistanceUnit(name, symbol, link)
  
  class DistanceQuantity(magnitude: BigDecimal, unit: DistanceUnit) extends QuantityImpl(magnitude, unit)

  def newQuantity(magnitude: BigDecimal, unit: DistanceUnit): DistanceQuantity = new DistanceQuantity(magnitude, unit)

  def conversionGraph() = _conversionGraph
  
  val wikipediaUrl = "http://en.wikipedia.org/wiki/Orders_of_magnitude_(length)"
  // "http://en.wikipedia.org/wiki/Distance"

  lazy val _conversionGraph = JungDirectedGraph[DistanceUnit, BigDecimal](
    List(
    ),
    (vs: Seq[JungDirectedGraphVertex[DistanceUnit]]) => vs match {
      case Nil => List(
      )
    }
  )
  // TODO link(mile, "1.609344", kilometer)

  lazy val foot = unit("foot", "ft")
  lazy val ft = foot
  lazy val mile = quantity("5280", foot, Some("mile"), Some("m"), Some("http://en.wikipedia.org/wiki/Mile"))
  lazy val meter = unit("meter", "m")
  lazy val kilometer = meter kilo
  lazy val km = kilometer
  lazy val centimeter = meter centi
  lazy val cm = centimeter
  lazy val millimeter = meter milli
  lazy val mm = millimeter
  lazy val micrometer = meter micro
  lazy val μm = micrometer
  lazy val nanometer = meter nano
  lazy val nm = nanometer
  lazy val ny2LA = quantity("2443.79", mile, Some("NY to LA"), None, Some("http://www.mapcrow.info/Distance_between_New_York_US_and_Los_Angeles_US.html"))
  lazy val au = quantity("92955807.3", mile, Some("Astronomical Unit"), Some("AU"), Some("http://en.wikipedia.org/wiki/Astronomical_unit"))
  lazy val auSI = quantity("149597870.7", kilometer, Some("Astronomical Unit"), Some("AU"), Some("http://en.wikipedia.org/wiki/Astronomical_unit"))
  lazy val lightyear = quantity("9460730472580.8", kilometer, Some("Light Year"), Some("ly"), Some("http://en.wikipedia.org/wiki/Light-year"))
  lazy val parsec = quantity("3.26", lightyear, Some("Parsec"), Some("pc"), Some("http://en.wikipedia.org/wiki/Parsec"))
  lazy val milkyWayDiameter = quantity(oneBD.scaleByPowerOfTen(5), lightyear, Some("Milky Way Diameter"), None, Some("http://en.wikipedia.org/wiki/Milky_Way"))
  lazy val toAndromeda = quantity("2.6E6", lightyear, Some("Distance to Andromeda"), None, Some("http://en.wikipedia.org/wiki/Andromeda_Galaxy"))

}

object Distance extends Distance()
