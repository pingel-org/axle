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
      unit("nanometer", "nm"),
      unit("Astronomical Unit", "AU", Some("http://en.wikipedia.org/wiki/Astronomical_unit")),
      unit("Astronomical Unit (SI)", "AU", Some("http://en.wikipedia.org/wiki/Astronomical_unit")),
      unit("light year", "ly", Some("http://en.wikipedia.org/wiki/Light-year")),
      unit("parsec", "pc", Some("http://en.wikipedia.org/wiki/Parsec"))
    ),
    (vs: Seq[JungDirectedGraphVertex[DistanceUnit]]) => vs match {
      case ft :: mile :: meter :: km :: cm :: mm :: μm :: nm :: au :: ausi :: ly :: pc :: Nil => List(
        (ft, mile, 5280),
        (km, mile, "1.609344"),
        (meter, km, "1E3"),
        (cm, meter, "1E2"),
        (mm, meter, "1E3"),
        (μm, meter, "1E6"),
        (nm, meter, "1E9"),
        (mile, au, "92955807.3"),
        (km, ausi, "149597870.7"),
        (km, ly, "9460730472580.8"),
        (ly, pc, "3.26")
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
  lazy val au = byName("Astronomical Unit")
  lazy val auSI = byName("Astronomical Unit (SI)")
  lazy val lightyear = byName("light year")
  lazy val parsec = byName("parsec")

}

object Distance extends Distance()
