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

  val unitsOfMeasurement = List(
    foot, mile,
    meter, kilometer, centimeter, millimeter, micrometer, nanometer
  )

  val derivations = Nil

  // these Quantities are often used as UnitsOfMeasurement
  
  val au = Quantity(92955807.3, mile, Some("Astronomical Unit"), Some("http://en.wikipedia.org/wiki/Astronomical_unit"))
  val auSI = Quantity(149597870.7, kilometer, Some("Astronomical Unit"), Some("http://en.wikipedia.org/wiki/Astronomical_unit"))

  val lightyear = Quantity(9460730472580.8, kilometer, Some("Light Year"), Some("http://en.wikipedia.org/wiki/Light-year"))
  
  val examples = List(
    Quantity(2443.79, mile, Some("NY to LA"), Some("http://www.mapcrow.info/Distance_between_New_York_US_and_Los_Angeles_US.html")),
    au,
    auSI
  )

}