package org.pingel.axle.quanta

import java.math.BigDecimal

class Speed extends Quantum {

  type UOM = SpeedUnit

  class SpeedUnit(
    baseUnit: Option[UOM] = None,
    magnitude: BigDecimal,
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None)
    extends UnitOfMeasurement(baseUnit, magnitude, name, symbol, link)


  def newUnitOfMeasurement(
    baseUnit: Option[UOM] = None,
    magnitude: BigDecimal,
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): SpeedUnit = new SpeedUnit(baseUnit, magnitude, name, symbol, link)
  
  import Distance.{meter, mile, ft}
  import Time.{second, hour}

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Speed"
  val derivations = List(Distance over Time)

  val mps = derive(meter.over[Time.type, this.type](second, this))
  val fps = derive(ft.over[Time.type, this.type](second, this))
  val mph = derive(mile.over[Time.type, this.type](hour, this))

  val c = quantity("299792458", mps, Some("Light Speed"), Some("c"), Some("http://en.wikipedia.org/wiki/Speed_of_light"))

  val speedLimit = quantity("65", mph, Some("Speed limit"), None)
  
}

object Speed extends Speed()