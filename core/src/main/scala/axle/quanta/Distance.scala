package axle.quanta

import java.math.BigDecimal
import axle.graph._

class Distance extends Quantum {

  class DistanceQuantity(
    magnitude: BigDecimal = oneBD,
    _unit: Option[Q] = None,
    _name: Option[String] = None,
    _symbol: Option[String] = None,
    _link: Option[String] = None) extends Quantity(magnitude, _unit, _name, _symbol, _link)

  type Q = DistanceQuantity

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): DistanceQuantity =
    new DistanceQuantity(oneBD, None, name, symbol, link)

  def newQuantity(magnitude: BigDecimal, unit: DistanceQuantity): DistanceQuantity =
    new DistanceQuantity(magnitude, Some(unit), None, None, None)

  def conversionGraph() = _conversionGraph

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Orders_of_magnitude_(length)"
  // "http://en.wikipedia.org/wiki/Distance"

  lazy val _conversionGraph = conversions(
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
    (vs: Seq[Vertex[DistanceQuantity]]) => vs match {
      case ft :: mile :: meter :: km :: cm :: mm :: μm :: nm :: au :: ausi :: ly :: pc :: Nil => withInverses(List(
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
      ))
      case _ => Nil
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

  lazy val ny2LA = "2443.79" *: mile // Some("NY to LA"), None, Some("http://www.mapcrow.info/Distance_between_New_York_US_and_Los_Angeles_US.html"))
  lazy val milkyWayDiameter = oneBD.scaleByPowerOfTen(5) *: lightyear // Some("Milky Way Diameter"), None, Some("http://en.wikipedia.org/wiki/Milky_Way"))
  lazy val toAndromeda = "2.6E6" *: lightyear // Some("Distance to Andromeda"), None, Some("http://en.wikipedia.org/wiki/Andromeda_Galaxy"))

}

object Distance extends Distance()
