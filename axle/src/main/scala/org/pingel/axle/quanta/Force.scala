package org.pingel.axle.quanta

import java.math.BigDecimal

class Force extends Quantum {

  type UOM = ForceUnit

  class ForceUnit(
    baseUnit: Option[UOM] = None,
    magnitude: BigDecimal,
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None)
    extends UnitOfMeasurement(baseUnit, magnitude, name, symbol, link)
  
}

object Force extends Quantum {
  
  val wikipediaUrl = "http://en.wikipedia.org/wiki/Force"
  val derivations = List(Mass by Acceleration)

  val pound = unit("pound", "lb", Some("http://en.wikipedia.org/wiki/Pound-force"))
  val newton = unit("newton", "N", Some("http://en.wikipedia.org/wiki/Newton_(unit)"))
  val dyne = unit("dyne", "dyn", Some("http://en.wikipedia.org/wiki/Dyne"))
  
  // val lightBulb = Quantity("60", watt, Some("Light Bulb"), None, Some("Light Bulb"))
}