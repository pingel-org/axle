package org.pingel.axle.quanta

object Distance extends Quantum {

  import Quantity._

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Distance"
  val derivations = Nil
  
  val foot = UnitOfMeasurement(this, "foot", "ft")
  val mile = Quantity("5280", foot, Some("mile"), Some("m"), Some("http://en.wikipedia.org/wiki/Mile"))
  val meter = UnitOfMeasurement(this, "meter", "m")
  val kilometer = meter kilo
  val centimeter = meter centi
  val millimeter = meter milli
  val micrometer = meter micro // μ
  val nanometer = meter nano

  val ny2LA = Quantity("2443.79", mile, Some("NY to LA"), None, Some("http://www.mapcrow.info/Distance_between_New_York_US_and_Los_Angeles_US.html"))
  
  val au = Quantity("92955807.3", mile, Some("Astronomical Unit"), Some("AU"), Some("http://en.wikipedia.org/wiki/Astronomical_unit"))
  val auSI = Quantity("149597870.7", kilometer, Some("Astronomical Unit"), Some("AU"), Some("http://en.wikipedia.org/wiki/Astronomical_unit"))
  
  val lightyear = Quantity("9460730472580.8", kilometer, Some("Light Year"), Some("ly"), Some("http://en.wikipedia.org/wiki/Light-year"))

  val parsec = Quantity("3.26", lightyear, Some("Parsec"), Some("pc"), Some("http://en.wikipedia.org/wiki/Parsec"))
  
  val milkyWayDiameter = Quantity("100000", lightyear, Some("Milky Way Diameter"), None, Some("http://en.wikipedia.org/wiki/Milky_Way"))
  
  val toAndromeda = Quantity("2600000", lightyear, Some("Distance to Andromeda"), None, Some("http://en.wikipedia.org/wiki/Andromeda_Galaxy"))

}