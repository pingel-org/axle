package axle.quanta

import spire.algebra._
import spire.math._
import spire.implicits._
import axle.graph._

abstract class Volume[N: Field: Order: Eq](space: MetricSpace[N, Double]) extends Quantum[N](space) {
 
  class VolumeQuantity(
    magnitude: N = field.one,
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
    new VolumeQuantity(field.one, None, name, symbol, link)

  def newQuantity(magnitude: N, unit: VolumeQuantity): VolumeQuantity =
    new VolumeQuantity(magnitude, Some(unit), None, None, None)

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Volume"

}

object Volume extends Volume[Rational](rationalDoubleMetricSpace) {

  import Distance.{ meter, km, cm }
  import Area.{ m2, km2, cm2 }

  lazy val _conversionGraph = conversions(
    List(
      derive(m2.by[Distance.type, this.type](meter, this), Some("m3"), Some("m3")),
      derive(km2.by[Distance.type, this.type](km, this), Some("km3"), Some("km3")),
      derive(cm2.by[Distance.type, this.type](cm, this), Some("cm3"), Some("cm3")),
      unit("Great Lakes Volume", "Great Lakes Volume", Some("http://en.wikipedia.org/wiki/Great_Lakes")),
      unit("liter", "L", Some("http://en.wikipedia.org/wiki/Liter")), // TODO: also symbol ℓ
      unit("milliliter", "mL"),
      unit("wine bottle", "wineBottle", Some("http://en.wikipedia.org/wiki/Wine_bottle")),
      unit("magnum", "magnum"),
      unit("jeroboam", "jeroboam"),
      unit("rehoboam", "rehoboam"),
      unit("methuselah", "methuselah"),
      unit("salmanazar", "salmanazar"),
      unit("balthazar", "balthazar"),
      unit("nebuchadnezzar", "nebuchadnezzar")),
    (vs: Seq[Vertex[VolumeQuantity]]) => vs match {
      case m3 :: km3 :: cm3 :: greatLakes :: liter :: milliliter ::
        wineBottle :: magnum :: jeroboam :: rehoboam :: methuselah ::
        salmanazar :: balthazar :: nebuchadnezzar :: Nil => trips2fns(List(
        (km3, greatLakes, 22671),
        (milliliter, liter, 1000),
        (cm3, milliliter, 1),
        (milliliter, wineBottle, 750),
        (wineBottle, magnum, 2),
        (wineBottle, jeroboam, 4),
        (wineBottle, rehoboam, 6),
        (wineBottle, methuselah, 8),
        (wineBottle, salmanazar, 12),
        (wineBottle, balthazar, 16),
        (wineBottle, nebuchadnezzar, 20)))
      case _ => Nil
    })

  lazy val m3 = byName("m3")
  lazy val km3 = byName("km3")
  lazy val cm3 = byName("cm3")
  lazy val greatLakes = byName("Great Lakes Volume")
  lazy val L = byName("liter")
  lazy val mL = byName("milliliter")
  lazy val wineBottle = byName("wine bottle")
  lazy val magnum = byName("magnum")
  lazy val jeroboam = byName("jeroboam")
  lazy val rehoboam = byName("rehoboam")
  lazy val methuselah = byName("methuselah")
  lazy val salmanazar = byName("salmanazar")
  lazy val balthazar = byName("balthazar")
  lazy val nebuchadnezzar = byName("nebuchadnezzar")

  def conversionGraph: DirectedGraph[Q, Rational => Rational] = _conversionGraph

}

