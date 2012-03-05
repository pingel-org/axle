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
 
  
}

object Volume extends Volume {

  import Distance.{meter, kilometer}
  
  val wikipediaUrl = "http://en.wikipedia.org/wiki/Volume"
    
  val derivations = List(Distance cubed)

  val m3 = derive(meter cubed)
  
  val km3 = derive(kilometer cubed)
  
  val greatLakes = quantity("22671", km3, Some("Great Lakes Volume"), None, Some("http://en.wikipedia.org/wiki/Great_Lakes"))
  
}