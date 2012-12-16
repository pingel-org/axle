package axle.quanta

import java.math.BigDecimal
import axle.graph._

class Area extends Quantum {

  class AreaQuantity(
    magnitude: BigDecimal = oneBD,
    _unit: Option[Q] = None,
    _name: Option[String] = None,
    _symbol: Option[String] = None,
    _link: Option[String] = None) extends Quantity(magnitude, _unit, _name, _symbol, _link)

  type Q = AreaQuantity

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): AreaQuantity =
    new AreaQuantity(oneBD, None, name, symbol, link)

  def newQuantity(magnitude: BigDecimal, unit: AreaQuantity): AreaQuantity =
    new AreaQuantity(magnitude, Some(unit), None, None, None)

  def conversionGraph() = _conversionGraph

  import Distance.{ meter, km }

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Area"

  lazy val _conversionGraph = conversions(
    List(
      derive(meter.by[Distance.type, this.type](meter, this), Some("m2"), Some("m2")),
      derive(km.by[Distance.type, this.type](km, this), Some("km2"), Some("km2"))
    ),
    (vs: Seq[DirectedGraphVertex[AreaQuantity]]) => vs match {
      case m2 :: km2 :: Nil => withInverses(List(
        (m2, km2, "1E6")
      ))
    }
  )

  lazy val m2 = byName("m2")
  lazy val km2 = byName("km2")

}

object Area extends Area()
