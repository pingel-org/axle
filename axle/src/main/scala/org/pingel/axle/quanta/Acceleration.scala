package org.pingel.axle.quanta

import java.math.BigDecimal

class Acceleration extends Quantum {

  type UOM = AccelerationUnit

  class AccelerationUnit(
    baseUnit: Option[UOM] = None,
    magnitude: BigDecimal,
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None)
    extends UnitOfMeasurement(baseUnit, magnitude, name, symbol, link)

}

object Acceleration extends Quantum {

  import Distance.{meter, foot}
  import Time.{second}
  
  val wikipediaUrl = "http://en.wikipedia.org/wiki/Acceleration"

  val derivations = List(Distance over (Time squared))

  val mpsps = derive(meter over (second squared))
  val fpsps = derive(foot over (second squared))

  val g = quantity("9.80665", mpsps, Some("g"), Some("g"), Some("http://en.wikipedia.org/wiki/Standard_gravity"))

  val examples = List(g)

}