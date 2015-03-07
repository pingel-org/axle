package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Mass[N]() extends Quantum4[N] {

  type Q = Mass[N]

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Orders_of_magnitude_(mass)"
  // "http://en.wikipedia.org/wiki/Mass"

  def unit(name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement4[Q, N](name, symbol, wiki)

  def gram: UnitOfMeasurement4[Q, N] = unit("gram", "g")
  def tonne: UnitOfMeasurement4[Q, N] = unit("tonne", "T", Some("http://en.wikipedia.org/wiki/Tonne"))
  def milligram: UnitOfMeasurement4[Q, N] = unit("milligram", "mg")
  def kilogram: UnitOfMeasurement4[Q, N] = unit("kilogram", "Kg")
  def megagram: UnitOfMeasurement4[Q, N] = unit("megagram", "Mg")
  def kilotonne: UnitOfMeasurement4[Q, N] = unit("kilotonne", "KT")
  def megatonne: UnitOfMeasurement4[Q, N] = unit("megatonne", "MT")
  def gigatonne: UnitOfMeasurement4[Q, N] = unit("gigatonne", "GT")
  def teratonne: UnitOfMeasurement4[Q, N] = unit("teratonne", "TT")
  def petatonne: UnitOfMeasurement4[Q, N] = unit("petatonne", "PT")
  def exatonne: UnitOfMeasurement4[Q, N] = unit("exatonne", "ET")
  def zettatonne: UnitOfMeasurement4[Q, N] = unit("zettatonne", "ZT")
  def yottatonne: UnitOfMeasurement4[Q, N] = unit("yottatonne", "YT")

  def man: UnitOfMeasurement4[Q, N] = unit("man", "man", Some("http://en.wikipedia.org/wiki/Body_weight"))

  def earth: UnitOfMeasurement4[Q, N] = unit("earth", "M⊕", Some("http://en.wikipedia.org/wiki/Earth"))
  def sun: UnitOfMeasurement4[Q, N] = unit("sun", "M☉", Some("http://en.wikipedia.org/wiki/Solar_mass"))
  def jupiter: UnitOfMeasurement4[Q, N] = unit("jupiter", "M♃", Some("http://en.wikipedia.org/wiki/Jupiter"))
  def saturn: UnitOfMeasurement4[Q, N] = unit("saturn", "M♄", Some("http://en.wikipedia.org/wiki/Saturn"))
  def neptune: UnitOfMeasurement4[Q, N] = unit("neptune", "M♆", Some("http://en.wikipedia.org/wiki/Neptune"))
  def uranus: UnitOfMeasurement4[Q, N] = unit("uranus", "M♅", Some("http://en.wikipedia.org/wiki/Uranus"))
  def venus: UnitOfMeasurement4[Q, N] = unit("venus", "M♀", Some("http://en.wikipedia.org/wiki/Venus"))
  def mars: UnitOfMeasurement4[Q, N] = unit("mars", "M♂", Some("http://en.wikipedia.org/wiki/Mars"))
  def mercury: UnitOfMeasurement4[Q, N] = unit("mercury", "M☿", Some("http://en.wikipedia.org/wiki/Mercury_(planet)"))
  def pluto: UnitOfMeasurement4[Q, N] = unit("pluto", "M♇", Some("http://en.wikipedia.org/wiki/Pluto"))
  def moon: UnitOfMeasurement4[Q, N] = unit("moon", "M☽", Some("http://en.wikipedia.org/wiki/Moon"))

  // http://en.wikipedia.org/wiki/Astronomical_symbols
  def ⊕ = earth
  def ☼ = sun
  def ☉ = sun
  def ♃ = jupiter
  def ♄ = saturn
  def ♆ = neptune
  def ♅ = uranus
  def ♀ = venus
  def ♂ = mars
  def ☿ = mercury
  def ♇ = pluto
  def ☽ = moon

  //  // sun also = "332950" *: earth
  //  // TODO lazy val milkyWayMass = 5.8E11 *: sun // Some("Milky Way Mass"), None, Some("http://en.wikipedia.org/wiki/Milky_Way"))
  //  // TODO lazy val andromedaMass = 7.1E11 *: sun // Some("Andromeda Mass"), None, Some("http://en.wikipedia.org/wiki/Andromeda_Galaxy"))
  //
  //  // TODO hydrogen atom
  //
  //  // earthunit = 5.9 x 10^24 kg
  //  // 10^24 kg = ^21 t = ^12 gt = ^9 tt = ^6 pt = ^3 et = ^0 zt

  def units: List[UnitOfMeasurement4[Q, N]] =
    List(gram, tonne, milligram, kilogram, megagram, kilotonne, megatonne, gigatonne, teratonne,
      petatonne, exatonne, zettatonne, yottatonne, man, earth, sun, jupiter, saturn, neptune,
      uranus, venus, mars, mercury, pluto, moon)

  def links(implicit fn: Field[N]): Seq[(UnitOfMeasurement4[Q, N], UnitOfMeasurement4[Q, N], Bijection[N, N])] =
    List[(UnitOfMeasurement4[Q, N], UnitOfMeasurement4[Q, N], Bijection[N, N])](
      (tonne, megagram, BijectiveIdentity[N]),
      (milligram, gram, Scale10s(3)),
      (gram, kilogram, Scale10s(3)),
      (gram, megagram, Scale10s(6)),
      (tonne, kilotonne, Scale10s(3)),
      (tonne, megatonne, Scale10s(6)),
      (tonne, gigatonne, Scale10s(9)),
      (tonne, teratonne, Scale10s(12)),
      (tonne, petatonne, Scale10s(15)),
      (tonne, exatonne, Scale10s(18)),
      (tonne, zettatonne, Scale10s(21)),
      (tonne, yottatonne, Scale10s(24)),
      (kilogram, man, ScaleDouble(86.6)),
      (zettatonne, earth, ScaleDouble(5.9736)),
      (kilogram, sun, ScaleDouble(1.9891E30)),
      (yottatonne, jupiter, ScaleDouble(1.8986)),
      (zettatonne, saturn, ScaleDouble(568.46)),
      (zettatonne, neptune, ScaleDouble(102.43)),
      (zettatonne, uranus, ScaleDouble(86.810)),
      (zettatonne, venus, ScaleDouble(4.868)),
      (exatonne, mars, ScaleDouble(641.85)),
      (exatonne, mercury, ScaleDouble(330.22)),
      (exatonne, pluto, ScaleDouble(13.05)),
      (exatonne, moon, ScaleDouble(73.477)))

}