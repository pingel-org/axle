package axle.quanta

import java.math.BigDecimal

class Distance extends Quantum {

  type UOM = DistanceUnit

  class DistanceUnit(
    conversion: Option[CGE] = None,
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None)
    extends UnitOfMeasurementImpl(conversion, name, symbol, link)

  def newUnitOfMeasurement(
    conversion: Option[CGE] = None,
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): DistanceUnit = new DistanceUnit(conversion, name, symbol, link)

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Orders_of_magnitude_(length)"
  // "http://en.wikipedia.org/wiki/Distance"

  val derivations = Nil

  val foot = unit("foot", "ft")
  val ft = foot

  val mile = quantity("5280", foot, Some("mile"), Some("m"), Some("http://en.wikipedia.org/wiki/Mile"))
  val meter = unit("meter", "m")

  val kilometer = meter kilo
  val km = kilometer

  link(mile, "1.609344", kilometer)

  val centimeter = meter centi
  val cm = centimeter

  val millimeter = meter milli
  val mm = millimeter

  val micrometer = meter micro
  val μm = micrometer

  val nanometer = meter nano
  val nm = nanometer

  val ny2LA = quantity("2443.79", mile, Some("NY to LA"), None, Some("http://www.mapcrow.info/Distance_between_New_York_US_and_Los_Angeles_US.html"))

  val au = quantity("92955807.3", mile, Some("Astronomical Unit"), Some("AU"), Some("http://en.wikipedia.org/wiki/Astronomical_unit"))
  val auSI = quantity("149597870.7", kilometer, Some("Astronomical Unit"), Some("AU"), Some("http://en.wikipedia.org/wiki/Astronomical_unit"))

  val lightyear = quantity("9460730472580.8", kilometer, Some("Light Year"), Some("ly"), Some("http://en.wikipedia.org/wiki/Light-year"))

  val parsec = quantity("3.26", lightyear, Some("Parsec"), Some("pc"), Some("http://en.wikipedia.org/wiki/Parsec"))

  val milkyWayDiameter = quantity(oneBD.scaleByPowerOfTen(5), lightyear, Some("Milky Way Diameter"), None, Some("http://en.wikipedia.org/wiki/Milky_Way"))

  val toAndromeda = quantity("2.6E6", lightyear, Some("Distance to Andromeda"), None, Some("http://en.wikipedia.org/wiki/Andromeda_Galaxy"))

}

object Distance extends Distance()
