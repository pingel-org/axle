package axle.quanta

import java.math.BigDecimal
import axle.graph.JungDirectedGraph._

class Force extends Quantum {

  type Q = ForceQuantity
  type UOM = ForceUnit

  class ForceUnit(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None)
    extends UnitOfMeasurementImpl(name, symbol, link)

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): ForceUnit = new ForceUnit(name, symbol, link)

  class ForceQuantity(magnitude: BigDecimal, unit: ForceUnit) extends QuantityImpl(magnitude, unit)

  def newQuantity(magnitude: BigDecimal, unit: ForceUnit): ForceQuantity = new ForceQuantity(magnitude, unit)

  def conversionGraph() = _conversionGraph

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Force"

  lazy val _conversionGraph = JungDirectedGraph[ForceUnit, BigDecimal](
    List(
      unit("pound", "lb", Some("http://en.wikipedia.org/wiki/Pound-force")),
      unit("newton", "N", Some("http://en.wikipedia.org/wiki/Newton_(unit)")),
      unit("dyne", "dyn", Some("http://en.wikipedia.org/wiki/Dyne"))
    ),
    (vs: Seq[JungDirectedGraphVertex[ForceUnit]]) => vs match {
      case Nil => List()
    }
  )

  lazy val pound = byName("pound")
  lazy val newton = byName("newton")
  lazy val dyne = byName("dyne")

}

object Force extends Force()
