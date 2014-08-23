package axle.quanta

import spire.algebra._
import spire.math._
import spire.implicits._
import axle.graph._

abstract class MoneyFlow[N: Field: Order: Eq] extends Quantum[N] {

  class MoneyFlowQuantity(
    magnitude: N = field.one,
    _unit: Option[Q] = None,
    _name: Option[String] = None,
    _symbol: Option[String] = None,
    _link: Option[String] = None) extends Quantity(magnitude, _unit, _name, _symbol, _link)

  type Q = MoneyFlowQuantity

  implicit def eqTypeclass: Eq[Q] = new Eq[Q] {
    def eqv(x: Q, y: Q): Boolean =
      (x.magnitude === y.magnitude) &&
        ((x.unitOption.isDefined && y.unitOption.isDefined && (x.unitOption.get === y.unitOption.get)) ||
          (x.unitOption.isEmpty && y.unitOption.isEmpty && x.equals(y)))
  }

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): MoneyFlowQuantity =
    new MoneyFlowQuantity(field.one, None, name, symbol, link)

  def newQuantity(magnitude: N, unit: MoneyFlowQuantity): MoneyFlowQuantity =
    new MoneyFlowQuantity(magnitude, Some(unit), None, None, None)

  val wikipediaUrl = "http://en.wikipedia.org/wiki/" // TODO

}

object MoneyFlow extends MoneyFlow[Rational] {

  import Money.{ USD }
  import Time.{ hour }

  lazy val _conversionGraph = conversions(
    List(
      derive(USD.by[Time.type, this.type](hour, this), Some("$/hr"), Some("$/hr"))),
    (vs: Seq[Vertex[MoneyFlowQuantity]]) => vs match {
      case _ => trips2fns(List())
    })

  lazy val USDperHour = byName("$/hr")

  def conversionGraph: DirectedGraph[Q, Rational => Rational] = _conversionGraph

}

