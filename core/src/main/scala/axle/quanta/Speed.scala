package axle.quanta

import java.math.BigDecimal
import axle.graph._

class Speed extends Quantum {

  type UOM = SpeedUnit

  class SpeedUnit(
    conversion: Option[JungDirectedGraphEdge[UOM, BigDecimal]] = None,
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None)
    extends UnitOfMeasurementImpl(conversion, name, symbol, link)

  def newUnitOfMeasurement(
    conversion: Option[JungDirectedGraphEdge[UOM, BigDecimal]] = None,
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): SpeedUnit = new SpeedUnit(conversion, name, symbol, link)

  def zero() = new SpeedUnit(None, Some("zero"), Some("0"), None) with ZeroWithUnit

  import Distance.{meter, mile, ft}
  import Time.{second, hour}

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Speed"
    
  // val derivations = List(Distance.over(Time, this))

  val mps = derive(meter.over[Time.type, this.type](second, this))
  val fps = derive(ft.over[Time.type, this.type](second, this))
  val mph = derive(mile.over[Time.type, this.type](hour, this))

  val c = quantity("299792458", mps, Some("Light Speed"), Some("c"), Some("http://en.wikipedia.org/wiki/Speed_of_light"))

  val speedLimit = quantity("65", mph, Some("Speed limit"), None)
  
}

object Speed extends Speed()
