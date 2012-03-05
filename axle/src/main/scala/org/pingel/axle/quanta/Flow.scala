package org.pingel.axle.quanta

import java.math.BigDecimal

class Flow extends Quantum {

  type UOM = FlowUnit

  class FlowUnit(
    baseUnit: Option[UOM] = None,
    magnitude: BigDecimal,
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None)
    extends UnitOfMeasurement(baseUnit, magnitude, name, symbol, link)

}

object Flow extends Quantum {

  import Volume.{m3}
  import Time.{second}
  
  val wikipediaUrl = "http://en.wikipedia.org/wiki/Volumetric_flow_rate"
    
  val derivations = List(Volume over Time)

  val m3s = m3 over second

  val niagaraFalls = quantity("1834", m3s, Some("Niagara Falls Flow"), None, Some("http://en.wikipedia.org/wiki/Niagara_Falls"))
  
}