package org.pingel.axle.quanta

import java.math.BigDecimal

class Acceleration extends Quantum {

  type UOM = AccelerationUnit

  class AccelerationUnit(
    conversion: Option[CG#E] = None,
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None)
    extends UnitOfMeasurement(conversion, name, symbol, link)

  def newUnitOfMeasurement(
    conversion: Option[CG#E] = None,
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): AccelerationUnit = new AccelerationUnit(conversion, name, symbol, link)

  import Speed.{mps, fps}
  import Time.{second}
  
  val wikipediaUrl = "http://en.wikipedia.org/wiki/Acceleration"

  // val derivations = List(Speed.over(Time, this))

  val mpsps = derive(mps.over[Time.type, this.type](second, this) )
  
  val fpsps = derive(fps.over[Time.type, this.type](second, this) )

  val g = quantity("9.80665", mpsps, Some("g"), Some("g"), Some("http://en.wikipedia.org/wiki/Standard_gravity"))

  val examples = List(g)
  
}

object Acceleration extends Acceleration()
