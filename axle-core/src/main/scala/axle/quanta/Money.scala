package axle.quanta

import spire.algebra._
import spire.math._
import spire.implicits._
import axle.graph._

class Money extends Quantum {

  class MoneyQuantity(
    magnitude: Number = one,
    _unit: Option[Q] = None,
    _name: Option[String] = None,
    _symbol: Option[String] = None,
    _link: Option[String] = None) extends Quantity(magnitude, _unit, _name, _symbol, _link)

  type Q = MoneyQuantity

  implicit def eqTypeclass: Eq[Q] = new Eq[Q] {
    def eqv(x: Q, y: Q): Boolean =
      (x.magnitude === y.magnitude) &&
        ((x.unitOption.isDefined && y.unitOption.isDefined && (x.unitOption.get === y.unitOption.get)) ||
          (x.unitOption.isEmpty && y.unitOption.isEmpty && x.equals(y)))
  }

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): MoneyQuantity =
    new MoneyQuantity(one, None, name, symbol, link)

  def newQuantity(magnitude: Number, unit: MoneyQuantity): MoneyQuantity =
    new MoneyQuantity(magnitude, Some(unit), None, None, None)

  def conversionGraph: DirectedGraph[Q, Number => Number] = _conversionGraph

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Money"

  lazy val _conversionGraph = conversions(
    List(
      unit("US Dollar", "USD")),
    (vs: Seq[Vertex[MoneyQuantity]]) => vs match {
      case _ => trips2fns(List())
    })

  lazy val USD = byName("US Dollar")

}

object Money extends Money()
