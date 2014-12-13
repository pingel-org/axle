package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case object Mass extends Quantum {

  type Q = Mass.type

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Orders_of_magnitude_(mass)"
  // "http://en.wikipedia.org/wiki/Mass"

  def unit[N](name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement[Q, N](name, symbol, wiki)

  def gram[N]: UnitOfMeasurement[Q, N] = unit("gram", "g")
  def tonne[N]: UnitOfMeasurement[Q, N] = unit("tonne", "T", Some("http://en.wikipedia.org/wiki/Tonne"))
  def milligram[N]: UnitOfMeasurement[Q, N] = unit("milligram", "mg")
  def kilogram[N]: UnitOfMeasurement[Q, N] = unit("kilogram", "Kg")
  def megagram[N]: UnitOfMeasurement[Q, N] = unit("megagram", "Mg")
  def kilotonne[N]: UnitOfMeasurement[Q, N] = unit("kilotonne", "KT")
  def megatonne[N]: UnitOfMeasurement[Q, N] = unit("megatonne", "MT")
  def gigatonne[N]: UnitOfMeasurement[Q, N] = unit("gigatonne", "GT")
  def teratonne[N]: UnitOfMeasurement[Q, N] = unit("teratonne", "TT")
  def petatonne[N]: UnitOfMeasurement[Q, N] = unit("petatonne", "PT")
  def exatonne[N]: UnitOfMeasurement[Q, N] = unit("exatonne", "ET")
  def zettatonne[N]: UnitOfMeasurement[Q, N] = unit("zettatonne", "ZT")
  def yottatonne[N]: UnitOfMeasurement[Q, N] = unit("yottatonne", "YT")

  def man[N]: UnitOfMeasurement[Q, N] = unit("man", "man", Some("http://en.wikipedia.org/wiki/Body_weight"))

  def earth[N]: UnitOfMeasurement[Q, N] = unit("earth", "M⊕", Some("http://en.wikipedia.org/wiki/Earth"))
  def sun[N]: UnitOfMeasurement[Q, N] = unit("sun", "M☉", Some("http://en.wikipedia.org/wiki/Solar_mass"))
  def jupiter[N]: UnitOfMeasurement[Q, N] = unit("jupiter", "M♃", Some("http://en.wikipedia.org/wiki/Jupiter"))
  def saturn[N]: UnitOfMeasurement[Q, N] = unit("saturn", "M♄", Some("http://en.wikipedia.org/wiki/Saturn"))
  def neptune[N]: UnitOfMeasurement[Q, N] = unit("neptune", "M♆", Some("http://en.wikipedia.org/wiki/Neptune"))
  def uranus[N]: UnitOfMeasurement[Q, N] = unit("uranus", "M♅", Some("http://en.wikipedia.org/wiki/Uranus"))
  def venus[N]: UnitOfMeasurement[Q, N] = unit("venus", "M♀", Some("http://en.wikipedia.org/wiki/Venus"))
  def mars[N]: UnitOfMeasurement[Q, N] = unit("mars", "M♂", Some("http://en.wikipedia.org/wiki/Mars"))
  def mercury[N]: UnitOfMeasurement[Q, N] = unit("mercury", "M☿", Some("http://en.wikipedia.org/wiki/Mercury_(planet)"))
  def pluto[N]: UnitOfMeasurement[Q, N] = unit("pluto", "M♇", Some("http://en.wikipedia.org/wiki/Pluto"))
  def moon[N]: UnitOfMeasurement[Q, N] = unit("moon", "M☽", Some("http://en.wikipedia.org/wiki/Moon"))

  // http://en.wikipedia.org/wiki/Astronomical_symbols
  def ⊕[N] = earth[N]
  def ☼[N] = sun[N]
  def ☉[N] = sun[N]
  def ♃[N] = jupiter[N]
  def ♄[N] = saturn[N]
  def ♆[N] = neptune[N]
  def ♅[N] = uranus[N]
  def ♀[N] = venus[N]
  def ♂[N] = mars[N]
  def ☿[N] = mercury[N]
  def ♇[N] = pluto[N]
  def ☽[N] = moon[N]

  //  // sun also = "332950" *: earth
  //  // TODO lazy val milkyWayMass = 5.8E11 *: sun // Some("Milky Way Mass"), None, Some("http://en.wikipedia.org/wiki/Milky_Way"))
  //  // TODO lazy val andromedaMass = 7.1E11 *: sun // Some("Andromeda Mass"), None, Some("http://en.wikipedia.org/wiki/Andromeda_Galaxy"))
  //
  //  // TODO hydrogen atom
  //
  //  // earthunit = 5.9 x 10^24 kg
  //  // 10^24 kg = ^21 t = ^12 gt = ^9 tt = ^6 pt = ^3 et = ^0 zt

  def units[N]: List[UnitOfMeasurement[Q, N]] =
    List(gram, tonne, milligram, kilogram, megagram, kilotonne, megatonne, gigatonne, teratonne,
      petatonne, exatonne, zettatonne, yottatonne, man, earth, sun, jupiter, saturn, neptune,
      uranus, venus, mars, mercury, pluto, moon)

  def links[N: Field]: Seq[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])] =
    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])](
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