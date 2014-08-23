package axle.quanta

import spire.algebra._
import spire.math._
import spire.implicits._
import axle.graph._

abstract class Mass[N: Field: Order: Eq](space: MetricSpace[N, Double]) extends Quantum[N](space) {

  class MassQuantity(
    magnitude: N = field.one,
    _unit: Option[Q] = None,
    _name: Option[String] = None,
    _symbol: Option[String] = None,
    _link: Option[String] = None) extends Quantity(magnitude, _unit, _name, _symbol, _link)

  type Q = MassQuantity

  implicit def eqTypeclass: Eq[Q] = new Eq[Q] {
    def eqv(x: Q, y: Q): Boolean =
      (x.magnitude === y.magnitude) &&
        ((x.unitOption.isDefined && y.unitOption.isDefined && (x.unitOption.get === y.unitOption.get)) ||
          (x.unitOption.isEmpty && y.unitOption.isEmpty && x.equals(y)))
  }

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): MassQuantity =
    new MassQuantity(field.one, None, name, symbol, link)

  def newQuantity(magnitude: N, unit: MassQuantity): MassQuantity =
    new MassQuantity(magnitude, Some(unit), None, None, None)

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Orders_of_magnitude_(mass)"
  // "http://en.wikipedia.org/wiki/Mass"

}

object Mass extends Mass[Rational](rationalDoubleMetricSpace) {

  lazy val _conversionGraph = conversions(
    List(
      unit("gram", "g"),
      unit("tonne", "T", Some("http://en.wikipedia.org/wiki/Tonne")),
      unit("milligram", "mg"),
      unit("kilogram", "Kg"),
      unit("megagram", "Mg"),
      unit("kilotonne", "KT"),
      unit("megatonne", "MT"),
      unit("gigatonne", "GT"),
      unit("teratonne", "TT"),
      unit("petatonne", "PT"),
      unit("exatonne", "ET"),
      unit("zettatonne", "ZT"),
      unit("yottatonne", "YT"),
      unit("man", "man", Some("http://en.wikipedia.org/wiki/Body_weight")),
      unit("earth", "M⊕", Some("http://en.wikipedia.org/wiki/Earth")),
      unit("sun", "M☉", Some("http://en.wikipedia.org/wiki/Solar_mass")),
      unit("jupiter", "M♃", Some("http://en.wikipedia.org/wiki/Jupiter")),
      unit("saturn", "M♄", Some("http://en.wikipedia.org/wiki/Saturn")),
      unit("neptune", "M♆", Some("http://en.wikipedia.org/wiki/Neptune")),
      unit("uranus", "M♅", Some("http://en.wikipedia.org/wiki/Uranus")),
      unit("venus", "M♀", Some("http://en.wikipedia.org/wiki/Venus")),
      unit("mars", "M♂", Some("http://en.wikipedia.org/wiki/Mars")),
      unit("mercury", "M☿", Some("http://en.wikipedia.org/wiki/Mercury_(planet)")),
      unit("pluto", "M♇", Some("http://en.wikipedia.org/wiki/Pluto")),
      unit("moon", "M☽", Some("http://en.wikipedia.org/wiki/Moon"))),
    (vs: Seq[Vertex[MassQuantity]]) => vs match {
      case g :: t :: mg :: kg :: meg :: kt :: mt :: gt :: tt :: pt :: et :: zt :: yt ::
        man :: earth :: sun :: jupiter :: saturn :: neptune :: uranus ::
        venus :: mars :: mercury :: pluto :: moon :: Nil =>
        trips2fns(List(
          (t, meg, 1),
          (mg, g, 1E3),
          (g, kg, 1E3),
          (g, meg, 1E6),
          (t, kt, 1E3),
          (t, mt, 1E6),
          (t, gt, 1E9),
          (t, tt, 1E12),
          (t, pt, 1E15),
          (t, et, 1E18),
          (t, zt, 1E21),
          (t, yt, 1E24),
          (kg, man, 86.6),
          (zt, earth, 5.9736),
          (kg, sun, 1.9891E30),
          (yt, jupiter, 1.8986),
          (zt, saturn, 568.46),
          (zt, neptune, 102.43),
          (zt, uranus, 86.810),
          (zt, venus, 4.868),
          (et, mars, 641.85),
          (et, mercury, 330.22),
          (et, pluto, 13.05),
          (et, moon, 73.477)))
      case _ => Nil
    })

  lazy val gram = byName("gram")
  lazy val g = gram
  lazy val tonne = byName("tonne")
  lazy val milligram = byName("milligram")
  lazy val mg = milligram
  lazy val kilogram = byName("kilogram")
  lazy val kg = kilogram
  lazy val megagram = byName("megagram")
  lazy val kilotonne = byName("kilotonne")
  lazy val megatonne = byName("megatonne")
  lazy val gigatonne = byName("gigatonne")
  lazy val teratonne = byName("teratonne")
  lazy val petatonne = byName("petatonne")
  lazy val exatonne = byName("exatonne")
  lazy val zettatonne = byName("zettatonne")
  lazy val yottatonne = byName("yottatonne")
  lazy val man = byName("man")
  lazy val earth = byName("earth")
  lazy val sun = byName("sun")
  lazy val jupiter = byName("jupiter")
  lazy val saturn = byName("saturn")
  lazy val neptune = byName("neptune")
  lazy val uranus = byName("uranus")
  lazy val venus = byName("venus")
  lazy val mars = byName("mars")
  lazy val mercury = byName("mercury")
  lazy val pluto = byName("pluto")
  lazy val moon = byName("moon")

  // sun also = "332950" *: earth
  lazy val milkyWayMass = 5.8E11 *: sun // Some("Milky Way Mass"), None, Some("http://en.wikipedia.org/wiki/Milky_Way"))
  lazy val andromedaMass = 7.1E11 *: sun // Some("Andromeda Mass"), None, Some("http://en.wikipedia.org/wiki/Andromeda_Galaxy"))

  // TODO hydrogen atom

  // earthunit = 5.9 x 10^24 kg
  // 10^24 kg = ^21 t = ^12 gt = ^9 tt = ^6 pt = ^3 et = ^0 zt

  // http://en.wikipedia.org/wiki/Astronomical_symbols
  lazy val ⊕ = earth
  lazy val ☼ = sun
  lazy val ☉ = sun
  lazy val ♃ = jupiter
  lazy val ♄ = saturn
  lazy val ♆ = neptune
  lazy val ♅ = uranus
  lazy val ♀ = venus
  lazy val ♂ = mars
  lazy val ☿ = mercury
  lazy val ♇ = pluto
  lazy val ☽ = moon

  def conversionGraph: DirectedGraph[Q, Rational => Rational] = _conversionGraph

}

