package axle.quanta

import java.math.BigDecimal
import axle.graph.JungDirectedGraph._

class Area extends Quantum {

  type UOM = AreaUnit

  class AreaUnit(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None)
    extends UnitOfMeasurementImpl(name, symbol, link)

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): AreaUnit = new AreaUnit(name, symbol, link)

  def zero() = new AreaUnit(Some("zero"), Some("0"), None) with ZeroWithUnit

  import Distance.{ meter, km }

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Area"

  // val derivations = List(Distance.by(Distance, this))

  val m2 = derive(meter.by[Distance.type, this.type](meter, this))

  val km2 = derive(km.by[Distance.type, this.type](km, this))

}

object Area extends Area()
