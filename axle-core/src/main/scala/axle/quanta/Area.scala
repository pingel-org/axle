package axle.quanta

import spire.algebra._
import spire.math._
import spire.implicits._
import axle.graph._

abstract class Area[N: Field: Order: Eq](space: MetricSpace[N, Double]) extends Quantum[N](space) {
  
  class AreaQuantity(
    magnitude: N = field.one,
    _unit: Option[Q] = None,
    _name: Option[String] = None,
    _symbol: Option[String] = None,
    _link: Option[String] = None) extends Quantity(magnitude, _unit, _name, _symbol, _link)

  type Q = AreaQuantity

  implicit def eqTypeclass: Eq[Q] = new Eq[Q] {
    def eqv(x: Q, y: Q): Boolean =
      (x.magnitude === y.magnitude) &&
        ((x.unitOption.isDefined && y.unitOption.isDefined && (x.unitOption.get === y.unitOption.get)) ||
          (x.unitOption.isEmpty && y.unitOption.isEmpty && x.equals(y)))
  }

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): AreaQuantity =
    new AreaQuantity(field.one, None, name, symbol, link)

  def newQuantity(magnitude: N, unit: AreaQuantity): AreaQuantity =
    new AreaQuantity(magnitude, Some(unit), None, None, None)

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Area"

}

object Area extends Area[Rational](rationalDoubleMetricSpace) {

  import Distance.{ meter, km, cm }
  
  lazy val _conversionGraph = conversions(
    List(
      derive(meter.by[Distance.type, this.type](meter, this), Some("m2"), Some("m2")),
      derive(km.by[Distance.type, this.type](km, this), Some("km2"), Some("km2")),
      derive(cm.by[Distance.type, this.type](cm, this), Some("cm2"), Some("cm2"))),
    (vs: Seq[Vertex[AreaQuantity]]) => vs match {
      case m2 :: km2 :: cm2 :: Nil => trips2fns(List(
        (m2, km2, 1E6),
        (cm2, m2, 1E6)))
      case _ => Nil
    })

  lazy val m2 = byName("m2")
  lazy val km2 = byName("km2")
  lazy val cm2 = byName("cm2")

  def conversionGraph: DirectedGraph[Q, Rational => Rational] = _conversionGraph
  
}
