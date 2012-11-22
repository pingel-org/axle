package axle.quanta

import java.math.BigDecimal
import axle.graph.JungDirectedGraph._

class Force extends Quantum {

  type UOM = ForceUnit

  class ForceUnit(
    conversion: Option[JungDirectedGraphEdge[UOM, BigDecimal]] = None,
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None)
    extends UnitOfMeasurementImpl(conversion, name, symbol, link)

  def newUnitOfMeasurement(
    conversion: Option[JungDirectedGraphEdge[UOM, BigDecimal]] = None,
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): ForceUnit = new ForceUnit(conversion, name, symbol, link)

  def zero() = new ForceUnit(None, Some("zero"), Some("0"), None) with ZeroWithUnit
  
  val wikipediaUrl = "http://en.wikipedia.org/wiki/Force"
    
  // val derivations = List(Mass.by(Acceleration, this))

  val pound = unit("pound", "lb", Some("http://en.wikipedia.org/wiki/Pound-force"))
  val newton = unit("newton", "N", Some("http://en.wikipedia.org/wiki/Newton_(unit)"))
  val dyne = unit("dyne", "dyn", Some("http://en.wikipedia.org/wiki/Dyne"))
  
  // val lightBulb = Quantity("60", watt, Some("Light Bulb"), None, Some("Light Bulb"))  
}

object Force extends Force()
