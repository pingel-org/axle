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
      unit("foot", "ft"),
      unit("mile", "m", Some("http://en.wikipedia.org/wiki/Mile")),
      unit("meter", "m"),
      unit("kilometer", "km"),
      unit("centimeter", "cm"),
      unit("millimeter", "mm"),
      unit("micrometer", "μm"),
      unit("nanometer", "nm")
    ),
    (vs: Seq[JungDirectedGraphVertex[DistanceUnit]]) => vs match {
      case ft :: mile :: meter :: km :: cm :: mm :: μm :: nm :: Nil => List(
        (ft, mile, 5280),
        (km, mile, "1.609344"),
        (meter, km, "1E3"),
        (cm, meter, "1E2"),
        (mm, meter, "1E3"),
        (μm, meter, "1E6"),
        (nm, meter, "1E9")
      )
    }
  )

  lazy val foot = byName("foot")
  lazy val ft = foot
  lazy val mile = byName("mile")
  lazy val meter = byName("meter")
  lazy val kilometer = byName("kilometer")
  lazy val km = kilometer
  lazy val centimeter = byName("centimeter")
  lazy val cm = centimeter
  lazy val millimeter = byName("millimeter")
  lazy val mm = millimeter
  lazy val micrometer = byName("micrometer")
  lazy val μm = micrometer
  lazy val nanometer = byName("nanometer")
  lazy val nm = nanometer

//  lazy val au = quantity("92955807.3", mile, Some("Astronomical Unit"), Some("AU"), Some("http://en.wikipedia.org/wiki/Astronomical_unit"))
//  lazy val auSI = quantity("149597870.7", kilometer, Some("Astronomical Unit (SI)"), Some("AU"), Some("http://en.wikipedia.org/wiki/Astronomical_unit"))
//  lazy val lightyear = quantity("9460730472580.8", kilometer, Some("Light Year"), Some("ly"), Some("http://en.wikipedia.org/wiki/Light-year"))
//  lazy val parsec = quantity("3.26", lightyear, Some("Parsec"), Some("pc"), Some("http://en.wikipedia.org/wiki/Parsec"))

}

object Distance extends Distance()
