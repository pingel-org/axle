package org.pingel.axle.quanta

object Power extends Quantum {

  import Quantity._
  
  val wikipediaUrl = "http://en.wikipedia.org/wiki/Power_(physics)"

  val watt = UnitOfMeasurement(this, "watt", "w")
  val kilowatt = watt kilo
  val megawatt = watt mega
  val gigawatt = watt giga
  val milliwatt = watt milli

  val horsepower = UnitOfMeasurement(this, "horsepower", "hp")

  val unitsOfMeasurement = List(watt, kilowatt, megawatt, gigawatt, horsepower)

  val derivations = List(
    Energy / Time
  )

  val examples = List(
    Quantity("60", watt, Some("Light Bulb"), None, Some("Light Bulb")),
    Quantity("2080", megawatt, Some("Hoover Dam"), None, Some("http://en.wikipedia.org/wiki/Hoover_Dam")),
    Quantity("420", horsepower, Some("2012 Mustang GT"), None, Some("http://en.wikipedia.org/wiki/Ford_Mustang"))
  )

}