package org.pingel.axle.quanta

object Mass extends Quantum {

  import Quantity._
  
  val wikipediaUrl = "http://en.wikipedia.org/wiki/Mass"

  val gram = UnitOfMeasurement(this, "gram", "g")
  val milligram = gram milli
  val kilogram = gram kilo
  val megagram = gram mega
  val tonne = Quantity("1", megagram, Some("tonne"), Some("t"), Some("http://en.wikipedia.org/wiki/Tonne"))
  val kilotonne = tonne kilo
  val megatonne = tonne mega
  val gigatonne = tonne giga
  val teratonne = tonne tera
  val petatonne = tonne peta
  val exatonne = tonne exa
  val zettatonne = tonne zetta
  val yottatonne = tonne yotta

  val unitsOfMeasurement = List(gram, kilogram, milligram)

  val derivations = List(Energy / (Speed squared)) // via E=mc^2

  val man = Quantity("86.6", kilogram, Some("Average US Man"), None, Some("http://en.wikipedia.org/wiki/Body_weight"))
  
  // earthunit = 5.9 x 10^24 kg
  // 10^24 kg = ^21 t = ^12 gt = ^9 tt = ^6 pt = ^3 et = ^0 zt
  val earth = Quantity("5.9736", zettatonne, Some("Earth"), Some("⊕"), Some("http://en.wikipedia.org/wiki/Earth"))
  val ⊕ = earth
  
  // also 1.9891 x 10^30 kg
  val sun = Quantity("333000", earth, Some("Sun"), Some("☼"), Some("http://en.wikipedia.org/wiki/Sun"))
  val ☼ = sun

 // http://en.wikipedia.org/wiki/Astronomical_symbols
//  val ♃ = jupiter
//  val ♄ = saturn
//  val ♆ = neptune
//  val ♅ = uranus
//  val ♀ = venus
//  val ♂ = mars
//  val ☿ = mercury
  
  val examples = List(
      // hydrogen
      man,
      earth,
      sun
  )

}

