package org.pingel.axle.quanta

object Mass extends Quantum {

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Mass"

  val gram = UnitOfMeasurement(this, "gram", "g")
  val milligram = gram milli
  val kilogram = gram kilo
  val megagram = gram mega
  val tonne = UnitOfMeasurement(this, "tonne", "t")
  val kilotonne = tonne kilo
  val megatonne = tonne mega
  val gigatonne = tonne giga
  val teratonne = tonne tera
  val petatonne = tonne peta
  val exatonne = tonne exa
  val zettatonne = tonne zetta
  val yottatonne = tonne yotta

  // conversion: tonne = megagram

  val unitsOfMeasurement = List(gram, kilogram, milligram)

  val derivations = List(Energy / (Speed squared)) // via E=mc^2

  // earthunit = 5.9 x 10^24 kg
  // 10^24 kg = ^21 t = ^12 gt = ^9 tt = ^6 pt = ^3 et = ^0 zt
  val earth = Quantity(5.9736, zettatonne, Some("Earth"), Some("http://en.wikipedia.org/wiki/Earth"))
  
  // also 1.9891 x 10^30 kg
  val sun = Quantity(333000.0, earth, Some("Sun"), Some("http://en.wikipedia.org/wiki/Sun"))

  val examples = List(
      // hydrogen
      Quantity(86.6, kilogram, Some("Average US Man"), Some("http://en.wikipedia.org/wiki/Body_weight")),
      earth,
      sun
  )

}

