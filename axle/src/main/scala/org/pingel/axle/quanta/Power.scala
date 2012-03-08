package org.pingel.axle.quanta

import java.math.BigDecimal

class Power extends Quantum {

  type UOM = PowerUnit

  class PowerUnit(
    conversion: Option[Conversion] = None,
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None)
    extends UnitOfMeasurement(conversion, name, symbol, link)

  def newUnitOfMeasurement(
    conversion: Option[Conversion] = None,
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): PowerUnit = new PowerUnit(conversion, name, symbol, link)
  
  val wikipediaUrl = "http://en.wikipedia.org/wiki/Power_(physics)"
    
  // val derivations = List(Energy.over(Time, this))

  val watt = unit("watt", "w")
  val kilowatt = watt kilo
  val megawatt = watt mega
  val gigawatt = watt giga
  val milliwatt = watt milli
  val horsepower = unit("horsepower", "hp")
  
  val lightBulb = quantity("60", watt, Some("Light Bulb"), None, Some("Light Bulb"))
  val hooverDam = quantity("2080", megawatt, Some("Hoover Dam"), None, Some("http://en.wikipedia.org/wiki/Hoover_Dam"))
  val mustangGT = quantity("420", horsepower, Some("2012 Mustang GT"), None, Some("http://en.wikipedia.org/wiki/Ford_Mustang"))
  
}

object Power extends Power()
