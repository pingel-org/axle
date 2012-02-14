package org.pingel.axle.quanta

object Power extends Quantum {

  import Quantity._
  
  val wikipediaUrl = "http://en.wikipedia.org/wiki/Power_(physics)"
  val derivations = List(Energy / Time)

  val watt = UnitOfMeasurement(this, "watt", "w")
  val kilowatt = watt kilo
  val megawatt = watt mega
  val gigawatt = watt giga
  val milliwatt = watt milli
  val horsepower = UnitOfMeasurement(this, "horsepower", "hp")
  
  val lightBulb = Quantity("60", watt, Some("Light Bulb"), None, Some("Light Bulb"))
  val hooverDam = Quantity("2080", megawatt, Some("Hoover Dam"), None, Some("http://en.wikipedia.org/wiki/Hoover_Dam"))
  val mustangGT = Quantity("420", horsepower, Some("2012 Mustang GT"), None, Some("http://en.wikipedia.org/wiki/Ford_Mustang"))

}