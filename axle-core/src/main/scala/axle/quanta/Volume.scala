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

  import Distance.{ meter, km, cm }
  import Area.{ m2, km2, cm2 }

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Volume"

  lazy val _conversionGraph = conversions(
    List(
      derive(m2.by[Distance.type, this.type](meter, this), Some("m3"), Some("m3")),
      derive(km2.by[Distance.type, this.type](km, this), Some("km3"), Some("km3")),
      derive(cm2.by[Distance.type, this.type](cm, this), Some("cm3"), Some("cm3")),
      unit("Great Lakes Volume", "Great Lakes Volume", Some("http://en.wikipedia.org/wiki/Great_Lakes")),
      unit("liter", "L", Some("http://en.wikipedia.org/wiki/Liter")), // TOOD: also symbol ℓ
      unit("milliliter", "mL")),
    (vs: Seq[Vertex[VolumeQuantity]]) => vs match {
      case m3 :: km3 :: cm3 :: greatLakes :: liter :: milliliter :: Nil => trips2fns(List(
        (km3, greatLakes, 22671),
        (milliliter, liter, 1000),
        (cm3, milliliter, 1)))
      case _ => Nil
    })

  lazy val m3 = byName("m3")
  lazy val km3 = byName("km3")
  lazy val cm3 = byName("cm3")
  lazy val greatLakes = byName("Great Lakes Volume")
  lazy val L = byName("liter")
  lazy val mL = byName("milliliter")

}

object Volume extends Volume
