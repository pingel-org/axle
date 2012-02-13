package org.pingel.axle.quanta

object Distance extends Quantum {

  val foot = UnitOfMeasurement(this, "foot", "ft")
  val mile = UnitOfMeasurement(this, "mile", "m")

  val meter = UnitOfMeasurement(this, "meter", "m")

  val kilometer = meter kilo
  val centimeter = meter centi
  val millimeter = meter milli
  val micrometer = meter micro // μ
  val nanometer = meter nano

  val derivations = Nil

  val au = Quantity(92955807.3, mile, Some("Astronomical Unit"), Some("AU"), Some("http://en.wikipedia.org/wiki/Astronomical_unit"))
  val auSI = Quantity(149597870.7, kilometer, Some("Astronomical Unit"), Some("AU"), Some("http://en.wikipedia.org/wiki/Astronomical_unit"))

  val lightyear = Quantity(9460730472580.8, kilometer, Some("Light Year"), Some("ly"), Some("http://en.wikipedia.org/wiki/Light-year"))

  val unitsOfMeasurement = List(
    foot, mile,
    meter, kilometer, centimeter, millimeter, micrometer, nanometer,
    lightyear, au, auSI
  )
  
  val examples = List(
    Quantity(2443.79, mile, Some("NY to LA"), None, Some("http://www.mapcrow.info/Distance_between_New_York_US_and_Los_Angeles_US.html")),
    au,
    auSI
  )

}