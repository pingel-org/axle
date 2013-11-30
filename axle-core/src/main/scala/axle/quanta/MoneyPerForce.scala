package axle.quanta

import spire.algebra._
import spire.math._
import axle.graph._

class MoneyPerForce extends Quantum {

  class MoneyPerForceQuantity(
    magnitude: Number = one,
    _unit: Option[Q] = None,
    _name: Option[String] = None,
    _symbol: Option[String] = None,
    _link: Option[String] = None) extends Quantity(magnitude, _unit, _name, _symbol, _link)

  type Q = MoneyPerForceQuantity

  implicit def eqTypeclass: Eq[Q] = new Eq[Q] {
    def eqv(x: Q, y: Q): Boolean = x equals y // TODO
  }
  
  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): MoneyPerForceQuantity =
    new MoneyPerForceQuantity(one, None, name, symbol, link)

  def newQuantity(magnitude: Number, unit: MoneyPerForceQuantity): MoneyPerForceQuantity =
    new MoneyPerForceQuantity(magnitude, Some(unit), None, None, None)

  def conversionGraph() = _conversionGraph

  val wikipediaUrl = "http://en.wikipedia.org/wiki/" // TODO

  import Money.{ USD }
  import Force.{ pound }

  lazy val _conversionGraph = conversions(
    List(
      derive(USD.by[Force.type, this.type](pound, this), Some("$/lb"), Some("$/lb"))),
    (vs: Seq[Vertex[MoneyPerForceQuantity]]) => vs match {
      case _ => trips2fns(List())
    })

  lazy val USDperPound = byName("$/lb")

}

object MoneyPerForce extends MoneyPerForce()
