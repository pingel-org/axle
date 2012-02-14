package org.pingel.axle.quanta

object Force extends Quantum {

  import Quantity._
  
  val wikipediaUrl = "http://en.wikipedia.org/wiki/Force"
  val derivations = List(Mass * Acceleration)

  val pound = UnitOfMeasurement(this, "pound", "lb", Some("http://en.wikipedia.org/wiki/Pound-force"))
  val newton = UnitOfMeasurement(this, "newton", "N", Some("http://en.wikipedia.org/wiki/Newton_(unit)"))
  val dyne = UnitOfMeasurement(this, "dyne", "dyn", Some("http://en.wikipedia.org/wiki/Dyne"))
  
  // val lightBulb = Quantity("60", watt, Some("Light Bulb"), None, Some("Light Bulb"))
}