package org.pingel.axle.quanta

import java.math.BigDecimal

object Acceleration extends Quantum {

  import Quantity._
  
  import Distance._
  import Time._
  
  val wikipediaUrl = "http://en.wikipedia.org/wiki/Acceleration"

  val mpsps = meter / (second squared)
  val fpsps = foot / (second squared)

  val unitsOfMeasurement = List(mpsps, fpsps)

  val derivations = List(mpsps.quantum)

  val g = Quantity("9.80665", mpsps, Some("g"), Some("g"), Some("http://en.wikipedia.org/wiki/Standard_gravity"))

  val examples = List(g)

}