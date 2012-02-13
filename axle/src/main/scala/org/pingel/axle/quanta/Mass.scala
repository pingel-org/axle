package org.pingel.axle.quanta

object Mass extends Quantum {

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Mass"

  val gram = UnitOfMeasurement(this, "gram", "g")
  val kilogram = gram kilo
  val megagram = gram mega
  val tonne = UnitOfMeasurement(this, "tonne", "t")
  val milligram = gram milli

  // conversion: tonne = megagram

  val unitsOfMeasurement = List(gram, kilogram, milligram)

  val derivations = List(Energy / (Speed squared)) // via E=mc^2

  val examples = List( // hydrogen
  // average male mass
  // earth
  // sun
  )

}

