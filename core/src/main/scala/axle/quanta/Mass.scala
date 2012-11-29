package axle.quanta

import java.math.BigDecimal
import axle.graph.JungDirectedGraph._

class Mass extends Quantum {

  class MassQuantity(
    magnitude: BigDecimal = oneBD,
    _unit: Option[Q] = None,
    _name: Option[String] = None,
    _symbol: Option[String] = None,
    _link: Option[String] = None) extends Quantity(magnitude, _unit, _name, _symbol, _link)

  type Q = MassQuantity

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): MassQuantity =
    new MassQuantity(oneBD, None, name, symbol, link)

  def newQuantity(magnitude: BigDecimal, unit: MassQuantity): MassQuantity =
    new MassQuantity(magnitude, Some(unit), None, None, None)

  def conversionGraph() = _conversionGraph

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Orders_of_magnitude_(mass)"
  // "http://en.wikipedia.org/wiki/Mass"

  lazy val _conversionGraph = JungDirectedGraph[MassQuantity, BigDecimal](
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
      unit("yottatonne", "YT")
    ),
    (vs: Seq[JungDirectedGraphVertex[MassQuantity]]) => vs match {
      case g :: t :: mg :: kg :: meg :: kt :: mt :: gt :: tt :: pt :: et :: zt :: yt :: Nil =>
        withInverses(List(
          (t, meg, 1),
          (mg, g, "1E3"),
          (g, kg, "1E3"),
          (g, meg, "1E6"),
          (t, kt, "1E3"),
          (t, mt, "1E6"),
          (t, gt, "1E9"),
          (t, tt, "1E12"),
          (t, pt, "1E15"),
          (t, et, "1E18"),
          (t, zt, "1E21"),
          (t, tt, "1E24")
        ))
    }
  )

  lazy val gram = byName("gram")
  lazy val tonne = byName("tonne")
  lazy val milligram = byName("milligram")
  lazy val kilogram = byName("kilogram")
  lazy val megagram = byName("megagram")
  lazy val kilotonne = byName("kilotonne")
  lazy val megatonne = byName("megatonne")
  lazy val gigatonne = byName("gigatonne")
  lazy val teratonne = byName("teratonne")
  lazy val petatonne = byName("petatonne")
  lazy val exatonne = byName("exatonne")
  lazy val zettatonne = byName("zettatonne")
  lazy val yottatonne = byName("yottatonne")

  // hydrogen atom
  lazy val man = "86.6" *: kilogram // Some("Average US Man"), None, Some("http://en.wikipedia.org/wiki/Body_weight"))

  // earthunit = 5.9 x 10^24 kg
  // 10^24 kg = ^21 t = ^12 gt = ^9 tt = ^6 pt = ^3 et = ^0 zt
  lazy val earth = "5.9736" *: zettatonne // Some("Earth"), Some("⊕"), Some("http://en.wikipedia.org/wiki/Earth"))
  lazy val ⊕ = earth

  // also 1.9891 x 10^30 kg
  lazy val sun = "332950" *: earth // Some("Sun"), Some("☉"), Some("http://en.wikipedia.org/wiki/Solar_mass"))
  lazy val ☼ = sun
  lazy val ☉ = sun

  // http://en.wikipedia.org/wiki/Astronomical_symbols

  lazy val jupiter = "1.8986" *: yottatonne // Some("Jupiter"), Some("♃"), Some("http://en.wikipedia.org/wiki/Jupiter"))
  lazy val ♃ = jupiter

  lazy val saturn = "568.46" *: zettatonne // Some("Saturn"), Some("♄"), Some("http://en.wikipedia.org/wiki/Saturn"))
  lazy val ♄ = saturn

  lazy val neptune = "102.43" *: zettatonne // Some("Neptune"), Some("♆"), Some("http://en.wikipedia.org/wiki/Neptune"))
  lazy val ♆ = neptune

  lazy val uranus = "86.810" *: zettatonne // Some("Uranus"), Some("♅"), Some("http://en.wikipedia.org/wiki/Uranus"))
  lazy val ♅ = uranus

  lazy val venus = "4.868" *: zettatonne // Some("Venus"), Some("♀"), Some("http://en.wikipedia.org/wiki/Venus"))
  lazy val ♀ = venus

  lazy val mars = "641.85" *: exatonne // Some("Mars"), Some("♂"), Some("http://en.wikipedia.org/wiki/Mars"))
  lazy val ♂ = mars

  lazy val mercury = "330.22" *: exatonne // Some("Mercury"), Some("☿"), Some("http://en.wikipedia.org/wiki/Mercury_(planet)"))
  lazy val ☿ = mercury

  lazy val pluto = "13.05" *: exatonne // Some("Pluto"), Some("♇"), Some("http://en.wikipedia.org/wiki/Pluto"))
  lazy val ♇ = pluto

  lazy val moon = "73.477" *: exatonne // Some("Moon"), Some("☽"), Some("http://en.wikipedia.org/wiki/Moon"))
  lazy val ☽ = moon

  lazy val milkyWayMass = "5.8E+11" *: sun // Some("Milky Way Mass"), None, Some("http://en.wikipedia.org/wiki/Milky_Way"))
  lazy val andromedaMass = "7.1E+11" *: sun // Some("Andromeda Mass"), None, Some("http://en.wikipedia.org/wiki/Andromeda_Galaxy"))

}

object Mass extends Mass()
