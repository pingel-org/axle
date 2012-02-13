package org.pingel.axle.quanta

object Acceleration extends Quantum {

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Acceleration"

  val mpsps = Distance.meter / (Time.second squared)
  val fpsps = Distance.foot / (Time.second squared)

  val unitsOfMeasurement = List(mpsps, fpsps)

  val derivations = List(mpsps.quantum)

  val g = Quantity(9.80665, mpsps, Some("g"), Some("g"), Some("http://en.wikipedia.org/wiki/Standard_gravity"))

  val examples = List(g)

}