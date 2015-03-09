package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Mass() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Orders_of_magnitude_(mass)"
  // "http://en.wikipedia.org/wiki/Mass"

}

trait MassMetadata[N] extends QuantumMetadata[Mass, N] {

  type U = UnitOfMeasurement[Mass, N]

  def gram: U
  def tonne: U
  def milligram: U
  def kilogram: U
  def megagram: U
  def kilotonne: U
  def megatonne: U
  def gigatonne: U
  def teratonne: U
  def petatonne: U
  def exatonne: U
  def zettatonne: U
  def yottatonne: U

  def man: U

  def earth: U
  def sun: U
  def jupiter: U
  def saturn: U
  def neptune: U
  def uranus: U
  def venus: U
  def mars: U
  def mercury: U
  def pluto: U
  def moon: U

  // http://en.wikipedia.org/wiki/Astronomical_symbols
  //  def ⊕: U
  //  def ☼: U
  //  def ☉: U
  //  def ♃: U
  //  def ♄: U
  //  def ♆: U
  //  def ♅: U
  //  def ♀: U
  //  def ♂: U
  //  def ☿: U
  //  def ♇: U
  //  def ☽: U

}

object Mass {

  def metadata[N] = new MassMetadata[N] {

    def unit(name: String, symbol: String, wiki: Option[String] = None) =
      UnitOfMeasurement[Mass, N](name, symbol, wiki)

    lazy val _gram = unit("gram", "g")
    lazy val _tonne = unit("tonne", "T", Some("http://en.wikipedia.org/wiki/Tonne"))
    lazy val _milligram = unit("milligram", "mg")
    lazy val _kilogram = unit("kilogram", "Kg")
    lazy val _megagram = unit("megagram", "Mg")
    lazy val _kilotonne = unit("kilotonne", "KT")
    lazy val _megatonne = unit("megatonne", "MT")
    lazy val _gigatonne = unit("gigatonne", "GT")
    lazy val _teratonne = unit("teratonne", "TT")
    lazy val _petatonne = unit("petatonne", "PT")
    lazy val _exatonne = unit("exatonne", "ET")
    lazy val _zettatonne = unit("zettatonne", "ZT")
    lazy val _yottatonne = unit("yottatonne", "YT")

    lazy val _man = unit("man", "man", Some("http://en.wikipedia.org/wiki/Body_weight"))

    lazy val _earth = unit("earth", "M⊕", Some("http://en.wikipedia.org/wiki/Earth"))
    lazy val _sun = unit("sun", "M☉", Some("http://en.wikipedia.org/wiki/Solar_mass"))
    lazy val _jupiter = unit("jupiter", "M♃", Some("http://en.wikipedia.org/wiki/Jupiter"))
    lazy val _saturn = unit("saturn", "M♄", Some("http://en.wikipedia.org/wiki/Saturn"))
    lazy val _neptune = unit("neptune", "M♆", Some("http://en.wikipedia.org/wiki/Neptune"))
    lazy val _uranus = unit("uranus", "M♅", Some("http://en.wikipedia.org/wiki/Uranus"))
    lazy val _venus = unit("venus", "M♀", Some("http://en.wikipedia.org/wiki/Venus"))
    lazy val _mars = unit("mars", "M♂", Some("http://en.wikipedia.org/wiki/Mars"))
    lazy val _mercury = unit("mercury", "M☿", Some("http://en.wikipedia.org/wiki/Mercury_(planet)"))
    lazy val _pluto = unit("pluto", "M♇", Some("http://en.wikipedia.org/wiki/Pluto"))
    lazy val _moon = unit("moon", "M☽", Some("http://en.wikipedia.org/wiki/Moon"))

    // http://en.wikipedia.org/wiki/Astronomical_symbols
    //    def ⊕ = earth
    //    def ☼ = sun
    //    def ☉ = sun
    //    def ♃ = jupiter
    //    def ♄ = saturn
    //    def ♆ = neptune
    //    def ♅ = uranus
    //    def ♀ = venus
    //    def ♂ = mars
    //    def ☿ = mercury
    //    def ♇ = pluto
    //    def ☽ = moon

    def gram = _gram
    def tonne = _tonne
    def milligram = _milligram
    def kilogram = _kilogram
    def megagram = _megagram
    def kilotonne = _kilotonne
    def megatonne = _megatonne
    def gigatonne = _gigatonne
    def teratonne = _teratonne
    def petatonne = _petatonne
    def exatonne = _exatonne
    def zettatonne = _zettatonne
    def yottatonne = _yottatonne

    def man = _man

    def earth = _earth
    def sun = _sun
    def jupiter = _jupiter
    def saturn = _saturn
    def neptune = _neptune
    def uranus = _uranus
    def venus = _venus
    def mars = _mars
    def mercury = _mercury
    def pluto = _pluto
    def moon = _moon

    //  // sun also = "332950" *: earth
    //  // TODO lazy val milkyWayMass = 5.8E11 *: sun // Some("Milky Way Mass"), None, Some("http://en.wikipedia.org/wiki/Milky_Way"))
    //  // TODO lazy val andromedaMass = 7.1E11 *: sun // Some("Andromeda Mass"), None, Some("http://en.wikipedia.org/wiki/Andromeda_Galaxy"))
    //
    //  // TODO hydrogen atom
    //
    //  // earthunit = 5.9 x 10^24 kg
    //  // 10^24 kg = ^21 t = ^12 gt = ^9 tt = ^6 pt = ^3 et = ^0 zt

    def units: List[UnitOfMeasurement[Mass, N]] =
      List(gram, tonne, milligram, kilogram, megagram, kilotonne, megatonne, gigatonne, teratonne,
        petatonne, exatonne, zettatonne, yottatonne, man, earth, sun, jupiter, saturn, neptune,
        uranus, venus, mars, mercury, pluto, moon)

    def links(implicit fn: Field[N]): Seq[(UnitOfMeasurement[Mass, N], UnitOfMeasurement[Mass, N], Bijection[N, N])] =
      List[(UnitOfMeasurement[Mass, N], UnitOfMeasurement[Mass, N], Bijection[N, N])](
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
}