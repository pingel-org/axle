package axle.quanta

import spire.algebra._
import spire.math._
import axle.graph._

class MoneyFlow extends Quantum {

  class MoneyFlowQuantity(
    magnitude: Number = one,
    _unit: Option[Q] = None,
    _name: Option[String] = None,
    _symbol: Option[String] = None,
    _link: Option[String] = None) extends Quantity(magnitude, _unit, _name, _symbol, _link)

  type Q = MoneyFlowQuantity

  implicit def eqTypeclass: Eq[Q] = new Eq[Q] {
    def eqv(x: Q, y: Q): Boolean = x equals y // TODO
  }
  
  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): MoneyFlowQuantity =
    new MoneyFlowQuantity(one, None, name, symbol, link)

  def newQuantity(magnitude: Number, unit: MoneyFlowQuantity): MoneyFlowQuantity =
    new MoneyFlowQuantity(magnitude, Some(unit), None, None, None)

  def conversionGraph() = _conversionGraph

  val wikipediaUrl = "http://en.wikipedia.org/wiki/" // TODO

  import Money.{ USD }
  import Time.{ hour }

  lazy val _conversionGraph = conversions(
    List(
      derive(USD.by[Time.type, this.type](hour, this), Some("$/hr"), Some("$/hr"))),
    (vs: Seq[Vertex[MoneyFlowQuantity]]) => vs match {
      case _ => trips2fns(List())
    })

  lazy val USDperHour = byName("$/hr")

}

object MoneyFlow extends MoneyFlow()
