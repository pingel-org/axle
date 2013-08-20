package axle.quanta

import spire.math._
import axle.graph._

class Force extends Quantum {

  class ForceQuantity(
    magnitude: Number = one,
    _unit: Option[Q] = None,
    _name: Option[String] = None,
    _symbol: Option[String] = None,
    _link: Option[String] = None) extends Quantity(magnitude, _unit, _name, _symbol, _link)

  type Q = ForceQuantity

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): ForceQuantity =
    new ForceQuantity(one, None, name, symbol, link)

  def newQuantity(magnitude: Number, unit: ForceQuantity): ForceQuantity =
    new ForceQuantity(magnitude, Some(unit), None, None, None)

  def conversionGraph() = _conversionGraph

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Force"

//  def vps() = List(
//    unit("pound", "lb", Some("http://en.wikipedia.org/wiki/Pound-force")),
//    unit("newton", "N", Some("http://en.wikipedia.org/wiki/Newton_(unit)")),
//    unit("dyne", "dyn", Some("http://en.wikipedia.org/wiki/Dyne"))
//  )
//
//  def ef() = (vs: Seq[DirectedGraphVertex[ForceQuantity]]) => vs match {
//    case Nil => withInverses(List())
//  }

  lazy val _conversionGraph = conversions(
    List(
      unit("pound", "lb", Some("http://en.wikipedia.org/wiki/Pound-force")),
      unit("newton", "N", Some("http://en.wikipedia.org/wiki/Newton_(unit)")),
      unit("dyne", "dyn", Some("http://en.wikipedia.org/wiki/Dyne"))
    ),
    (vs: Seq[Vertex[ForceQuantity]]) => vs match {
      case pound :: newton :: dyne :: Nil => trips2fns(List())
    }
  )

  lazy val pound = byName("pound")
  lazy val newton = byName("newton")
  lazy val dyne = byName("dyne")

}

object Force extends Force()
