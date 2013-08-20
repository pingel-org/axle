package axle.quanta

import spire.math._
import axle.graph._

class Money extends Quantum {

  class MoneyQuantity(
    magnitude: Number = one,
    _unit: Option[Q] = None,
    _name: Option[String] = None,
    _symbol: Option[String] = None,
    _link: Option[String] = None) extends Quantity(magnitude, _unit, _name, _symbol, _link)

  type Q = MoneyQuantity

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): MoneyQuantity =
    new MoneyQuantity(one, None, name, symbol, link)

  def newQuantity(magnitude: Number, unit: MoneyQuantity): MoneyQuantity =
    new MoneyQuantity(magnitude, Some(unit), None, None, None)

  def conversionGraph() = _conversionGraph

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Money"

  lazy val _conversionGraph = conversions(
    List(
      unit("US Dollar", "USD")
    ),
    (vs: Seq[Vertex[MoneyQuantity]]) => vs match {
      case _ => trips2fns(List())
    }
  )

  lazy val USD = byName("US Dollar")

}

object Money extends Money()