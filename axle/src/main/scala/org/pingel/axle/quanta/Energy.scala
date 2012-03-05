package org.pingel.axle.quanta

import java.math.BigDecimal

class Energy extends Quantum {

  type UOM = EnergyUnit

  class EnergyUnit(
    baseUnit: Option[UOM] = None,
    magnitude: BigDecimal,
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None)
    extends UnitOfMeasurement(baseUnit, magnitude, name, symbol, link)
 
  
}


object Energy extends Quantum {

  import Power.{kilowatt}
  import Time.{hour}
  
  val wikipediaUrl = "http://en.wikipedia.org/wiki/Energy"

  val derivations = List(Power by Time)

  val kwh = derive(kilowatt.by[Time.type, Energy.type](hour))
 
  val joule = unit("joule", "J")
  
  val kilojoule = joule kilo
  val megajoule = joule mega
  val ton = quantity("4.184", megajoule, Some("ton TNT"), Some("T"), Some("http://en.wikipedia.org/wiki/TNT_equivalent"))
  val kiloton = ton kilo
  val megaton = ton mega
  val gigaton = ton giga
  val castleBravo = quantity("15", megaton, Some("Castle Bravo Thermonuclear Bomb"), None, Some("http://en.wikipedia.org/wiki/Castle_Bravo"))

}