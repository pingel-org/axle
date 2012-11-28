package axle.quanta

import java.math.BigDecimal
import axle.graph.JungDirectedGraph._

class Area extends Quantum {

  type Q = AreaQuantity
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

  class AreaQuantity(magnitude: BigDecimal, unit: AreaUnit) extends QuantityImpl(magnitude, unit)

  def newQuantity(magnitude: BigDecimal, unit: AreaUnit): AreaQuantity = new AreaQuantity(magnitude, unit)

  def conversionGraph() = _conversionGraph

  import Distance.{ meter, km }

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Area"

  lazy val _conversionGraph = JungDirectedGraph[AreaUnit, BigDecimal](
    List(
      derive(meter.by[Distance.type, this.type](meter, this)),
      derive(km.by[Distance.type, this.type](km, this))
    ),
    (vs: Seq[JungDirectedGraphVertex[AreaUnit]]) => vs match {
      case m2 :: km2 :: Nil => List(
        (m2, km2, "1E6")
      )
    }
  )

  lazy val m2 = byName("m2")
  lazy val km2 = byName("km2")

}

object Area extends Area()
