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

  def newUnitOfMeasurement(
    baseUnit: Option[UOM] = None,
    magnitude: BigDecimal,
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): FlowUnit = new FlowUnit(baseUnit, magnitude, name, symbol, link)

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Volumetric_flow_rate"
    
  val derivations = List(Volume over Time)

  import Volume.{m3}
  import Time.{second}

  val m3s = m3.over[Time.type, this.type](second, this)

  val niagaraFalls = quantity("1834", m3s, Some("Niagara Falls Flow"), None, Some("http://en.wikipedia.org/wiki/Niagara_Falls"))
  
}

object Flow extends Flow()
