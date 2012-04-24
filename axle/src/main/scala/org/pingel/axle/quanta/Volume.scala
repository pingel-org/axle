package org.pingel.axle.quanta

import java.math.BigDecimal

class Volume extends Quantum {

  type UOM = VolumeUnit

  class VolumeUnit(
    conversion: Option[E] = None,
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None)
    extends UnitOfMeasurement(conversion, name, symbol, link)

  def newUnitOfMeasurement(
    conversion: Option[E] = None,
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): VolumeUnit = new VolumeUnit(conversion, name, symbol, link)

  import Distance.{meter, km}
  import Area.{m2, km2}
  
  val wikipediaUrl = "http://en.wikipedia.org/wiki/Volume"
    
  // val derivations = List(Area.by(Distance, this))

  val m3 = derive(m2.by[Distance.type, this.type](meter, this), Some("cubic meters"), Some("m^3"))
  
  val km3 = derive(km2.by[Distance.type, this.type](km, this), Some("cubic kilometers"), Some("km^3"))
  
  val greatLakes = quantity("22671", km3, Some("Great Lakes Volume"), None, Some("http://en.wikipedia.org/wiki/Great_Lakes"))
  
  
}

object Volume extends Volume()
