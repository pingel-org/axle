package axle.quanta

import java.math.BigDecimal
import axle.graph.JungDirectedGraph._

class Mass extends Quantum {

  type Q = MassQuantity
  type UOM = MassUnit

  class MassUnit(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None)
    extends UnitOfMeasurementImpl(name, symbol, link)

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): MassUnit = new MassUnit(name, symbol, link)

  class MassQuantity(magnitude: BigDecimal, unit: MassUnit) extends QuantityImpl(magnitude, unit)

  def newQuantity(magnitude: BigDecimal, unit: MassUnit): MassQuantity = new MassQuantity(magnitude, unit)

  def conversionGraph() = _conversionGraph

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Orders_of_magnitude_(mass)"
  // "http://en.wikipedia.org/wiki/Mass"

  lazy val _conversionGraph = JungDirectedGraph[MassUnit, BigDecimal](
    List(
    ),
    (vs: Seq[JungDirectedGraphVertex[MassUnit]]) => vs match {
      case Nil => List(
      )
    }
  )

  lazy val gram = unit("gram", "g")
  lazy val tonne = quantity("1", megagram, Some("tonne"), Some("t"), Some("http://en.wikipedia.org/wiki/Tonne"))
  
  lazy val milligram = gram milli
  lazy val kilogram = gram kilo
  lazy val megagram = gram mega
  lazy val kilotonne = tonne kilo
  lazy val megatonne = tonne mega
  lazy val gigatonne = tonne giga
  lazy val teratonne = tonne tera
  lazy val petatonne = tonne peta
  lazy val exatonne = tonne exa
  lazy val zettatonne = tonne zetta
  lazy val yottatonne = tonne yotta
  // hydrogen atom
 
  lazy val man = quantity("86.6", kilogram, Some("Average US Man"), None, Some("http://en.wikipedia.org/wiki/Body_weight"))
  
  // earthunit = 5.9 x 10^24 kg
  // 10^24 kg = ^21 t = ^12 gt = ^9 tt = ^6 pt = ^3 et = ^0 zt
  lazy val earth = quantity("5.9736", zettatonne, Some("Earth"), Some("⊕"), Some("http://en.wikipedia.org/wiki/Earth"))
  lazy val ⊕ = earth

  // also 1.9891 x 10^30 kg
  lazy val sun = quantity("332950", earth, Some("Sun"), Some("☉"), Some("http://en.wikipedia.org/wiki/Solar_mass"))
  lazy val ☼ = sun
  lazy val ☉ = sun

  // http://en.wikipedia.org/wiki/Astronomical_symbols
  
  lazy val jupiter = quantity("1.8986", yottatonne, Some("Jupiter"), Some("♃"), Some("http://en.wikipedia.org/wiki/Jupiter"))
  lazy val ♃ = jupiter

  lazy val saturn = quantity("568.46", zettatonne, Some("Saturn"), Some("♄"), Some("http://en.wikipedia.org/wiki/Saturn"))
  lazy val ♄ = saturn

  lazy val neptune = quantity("102.43", zettatonne, Some("Neptune"), Some("♆"), Some("http://en.wikipedia.org/wiki/Neptune"))
  lazy val ♆ = neptune
  
  lazy val uranus = quantity("86.810", zettatonne, Some("Uranus"), Some("♅"), Some("http://en.wikipedia.org/wiki/Uranus"))
  lazy val ♅ = uranus
  
  lazy val venus = quantity("4.868", zettatonne, Some("Venus"), Some("♀"), Some("http://en.wikipedia.org/wiki/Venus"))
  lazy val ♀ = venus
  
  lazy val mars = quantity("641.85", exatonne, Some("Mars"), Some("♂"), Some("http://en.wikipedia.org/wiki/Mars"))
  lazy val ♂ = mars
  
  lazy val mercury = quantity("330.22", exatonne, Some("Mercury"), Some("☿"), Some("http://en.wikipedia.org/wiki/Mercury_(planet)"))
  lazy val ☿ = mercury

  lazy val pluto = quantity("13.05", exatonne, Some("Pluto"), Some("♇"), Some("http://en.wikipedia.org/wiki/Pluto"))
  lazy val ♇ = pluto

  lazy val moon = quantity("73.477", exatonne, Some("Moon"), Some("☽"), Some("http://en.wikipedia.org/wiki/Moon"))
  lazy val ☽ = moon
  
  lazy val milkyWayMass = quantity("5.8E+11", sun, Some("Milky Way Mass"), None, Some("http://en.wikipedia.org/wiki/Milky_Way"))
  lazy val andromedaMass = quantity("7.1E+11", sun, Some("Andromeda Mass"), None, Some("http://en.wikipedia.org/wiki/Andromeda_Galaxy"))
  
}

object Mass extends Mass()
