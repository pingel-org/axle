package org.pingel.axle.quanta

object Energy extends Quantum {

  import Quantity._
  
  import Power._
  import Time._
  
  val wikipediaUrl = "http://en.wikipedia.org/wiki/Energy"

  val joule = UnitOfMeasurement(this, "joule", "J")
  val kilojoule = joule kilo
  val megajoule = joule mega

  val ton = Quantity("4.184", megajoule, Some("ton TNT"), Some("T"), Some("http://en.wikipedia.org/wiki/TNT_equivalent"))
  val kiloton = ton kilo
  val megaton = ton mega
  val gigaton = ton giga

  val kwh = kilowatt * hour

  val unitsOfMeasurement = List(
    joule,
    ton, kiloton, megaton, gigaton,
    kwh)

  val derivations = List(kwh.quantum)

  val examples = List(
    Quantity("15", megaton, Some("Castle Bravo Thermonuclear Bomb"), None, Some("http://en.wikipedia.org/wiki/Castle_Bravo"))
  )

}