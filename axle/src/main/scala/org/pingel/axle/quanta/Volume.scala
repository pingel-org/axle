package org.pingel.axle.quanta

import java.math.BigDecimal

class Volume extends Quantum {

  type UOM = VolumeUnit

  class VolumeUnit(
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
    link: Option[String] = None): VolumeUnit = new VolumeUnit(baseUnit, magnitude, name, symbol, link)

  import Distance.{meter, km}
  import Area.{m2, km2}
  
  val wikipediaUrl = "http://en.wikipedia.org/wiki/Volume"
    
  val derivations = List(Distance cubed)

  val m3 = derive(m2.by[Distance.type, this.type](meter, this))
  
  val km3 = derive(km2.by[Distance.type, this.type](km, this))
  
  val greatLakes = quantity("22671", km3, Some("Great Lakes Volume"), None, Some("http://en.wikipedia.org/wiki/Great_Lakes"))
  
  
}

object Volume extends Volume()
