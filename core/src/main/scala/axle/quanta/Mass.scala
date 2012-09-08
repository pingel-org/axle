package axle.quanta

import java.math.BigDecimal

class Mass extends Quantum {

  type UOM = MassUnit

  class MassUnit(
    conversion: Option[CGE] = None,
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None)
    extends UnitOfMeasurementImpl(conversion, name, symbol, link)

  def newUnitOfMeasurement(
    conversion: Option[CGE] = None,
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): MassUnit = new MassUnit(conversion, name, symbol, link)

  def zero() = new MassUnit(None, Some("zero"), Some("0"), None) with ZeroWithUnit

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Orders_of_magnitude_(mass)"
  // "http://en.wikipedia.org/wiki/Mass"
  val derivations = List() // Energy over (Speed by Speed)) // via E=mc^2

  val gram = unit("gram", "g")
  val milligram = gram milli
  val kilogram = gram kilo
  val megagram = gram mega
  val tonne = quantity("1", megagram, Some("tonne"), Some("t"), Some("http://en.wikipedia.org/wiki/Tonne"))
  val kilotonne = tonne kilo
  val megatonne = tonne mega
  val gigatonne = tonne giga
  val teratonne = tonne tera
  val petatonne = tonne peta
  val exatonne = tonne exa
  val zettatonne = tonne zetta
  val yottatonne = tonne yotta
  // hydrogen atom
  val man = quantity("86.6", kilogram, Some("Average US Man"), None, Some("http://en.wikipedia.org/wiki/Body_weight"))
  
  // earthunit = 5.9 x 10^24 kg
  // 10^24 kg = ^21 t = ^12 gt = ^9 tt = ^6 pt = ^3 et = ^0 zt
  val earth = quantity("5.9736", zettatonne, Some("Earth"), Some("⊕"), Some("http://en.wikipedia.org/wiki/Earth"))
  val ⊕ = earth

  // also 1.9891 x 10^30 kg
  val sun = quantity("332950", earth, Some("Sun"), Some("☉"), Some("http://en.wikipedia.org/wiki/Solar_mass"))
  val ☼ = sun
  val ☉ = sun

  // http://en.wikipedia.org/wiki/Astronomical_symbols
  
  val jupiter = quantity("1.8986", yottatonne, Some("Jupiter"), Some("♃"), Some("http://en.wikipedia.org/wiki/Jupiter"))
  val ♃ = jupiter

  val saturn = quantity("568.46", zettatonne, Some("Saturn"), Some("♄"), Some("http://en.wikipedia.org/wiki/Saturn"))
  val ♄ = saturn

  val neptune = quantity("102.43", zettatonne, Some("Neptune"), Some("♆"), Some("http://en.wikipedia.org/wiki/Neptune"))
  val ♆ = neptune
  
  val uranus = quantity("86.810", zettatonne, Some("Uranus"), Some("♅"), Some("http://en.wikipedia.org/wiki/Uranus"))
  val ♅ = uranus
  
  val venus = quantity("4.868", zettatonne, Some("Venus"), Some("♀"), Some("http://en.wikipedia.org/wiki/Venus"))
  val ♀ = venus
  
  val mars = quantity("641.85", exatonne, Some("Mars"), Some("♂"), Some("http://en.wikipedia.org/wiki/Mars"))
  val ♂ = mars
  
  val mercury = quantity("330.22", exatonne, Some("Mercury"), Some("☿"), Some("http://en.wikipedia.org/wiki/Mercury_(planet)"))
  val ☿ = mercury

  val pluto = quantity("13.05", exatonne, Some("Pluto"), Some("♇"), Some("http://en.wikipedia.org/wiki/Pluto"))
  val ♇ = pluto

  val moon = quantity("73.477", exatonne, Some("Moon"), Some("☽"), Some("http://en.wikipedia.org/wiki/Moon"))
  val ☽ = moon
  
  val milkyWayMass = quantity("5.8E+11", sun, Some("Milky Way Mass"), None, Some("http://en.wikipedia.org/wiki/Milky_Way"))
  val andromedaMass = quantity("7.1E+11", sun, Some("Andromeda Mass"), None, Some("http://en.wikipedia.org/wiki/Andromeda_Galaxy"))
  
}

object Mass extends Mass()
