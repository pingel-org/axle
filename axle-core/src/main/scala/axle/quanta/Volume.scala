package axle.quanta

import spire.algebra._
import spire.math._
import spire.implicits._
import axle.graph._

class Volume extends Quantum {

  class VolumeQuantity(
    magnitude: Number = one,
    _unit: Option[Q] = None,
    _name: Option[String] = None,
    _symbol: Option[String] = None,
    _link: Option[String] = None) extends Quantity(magnitude, _unit, _name, _symbol, _link)

  type Q = VolumeQuantity

  implicit def eqTypeclass: Eq[Q] = new Eq[Q] {
    def eqv(x: Q, y: Q): Boolean =
      (x.magnitude === y.magnitude) &&
        ((x.unitOption.isDefined && y.unitOption.isDefined && (x.unitOption.get === y.unitOption.get)) ||
          (x.unitOption.isEmpty && y.unitOption.isEmpty && x.equals(y)))
  }

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): VolumeQuantity =
    new VolumeQuantity(one, None, name, symbol, link)

  def newQuantity(magnitude: Number, unit: VolumeQuantity): VolumeQuantity =
    new VolumeQuantity(magnitude, Some(unit), None, None, None)

  def conversionGraph: DirectedGraph[Q, Number => Number] = _conversionGraph

  import Distance.{ meter, km }
  import Area.{ m2, km2 }

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Volume"

  lazy val _conversionGraph = conversions(
    List(
      derive(m2.by[Distance.type, this.type](meter, this), Some("m3"), Some("m3")),
      derive(km2.by[Distance.type, this.type](km, this), Some("km3"), Some("km3")),
      unit("Great Lakes Volume", "Great Lakes Volume", Some("http://en.wikipedia.org/wiki/Great_Lakes"))),
    (vs: Seq[Vertex[VolumeQuantity]]) => vs match {
      case m3 :: km3 :: greatLakes :: Nil => trips2fns(List(
        (km3, greatLakes, 22671)))
      case _ => Nil
    })

  lazy val m3 = byName("m3")
  lazy val km3 = byName("km3")
  lazy val greatLakes = byName("Great Lakes Volume")

}

object Volume extends Volume
