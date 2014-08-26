package axle.quanta2

import axle.graph.DirectedGraph
import axle.graph.Vertex
import spire.implicits.moduleOps
import spire.math.Rational
import spire.math.Rational.apply

class Distance extends Quantum {
  def wikipediaUrl = "http://en.wikipedia.org/wiki/Orders_of_magnitude_(length)"
}

object Distance extends Distance {
  
  implicit val cgDR: DirectedGraph[Quantity[Distance, Rational], Rational => Rational] = conversions(
    List(
      unit[Distance, Rational]("foot", "ft"),
      unit[Distance, Rational]("mile", "m", Some("http://en.wikipedia.org/wiki/Mile")),
      unit[Distance, Rational]("meter", "m"),
      unit[Distance, Rational]("kilometer", "km"),
      unit[Distance, Rational]("centimeter", "cm"),
      unit[Distance, Rational]("millimeter", "mm"),
      unit[Distance, Rational]("micrometer", "μm"),
      unit[Distance, Rational]("nanometer", "nm"),
      unit[Distance, Rational]("Astronomical Unit", "AU", Some("http://en.wikipedia.org/wiki/Astronomical_unit")),
      unit[Distance, Rational]("Astronomical Unit (SI)", "AU", Some("http://en.wikipedia.org/wiki/Astronomical_unit")),
      unit[Distance, Rational]("light year", "ly", Some("http://en.wikipedia.org/wiki/Light-year")),
      unit[Distance, Rational]("parsec", "pc", Some("http://en.wikipedia.org/wiki/Parsec"))),
    (vs: Seq[Vertex[Quantity[Distance, Rational]]]) => vs match {
      case ft :: mile :: meter :: km :: cm :: mm :: μm :: nm :: au :: ausi :: ly :: pc :: Nil => trips2fns[Distance, Rational](List(
        (ft, mile, 5280),
        (km, mile, 1.609344),
        (meter, km, 1E3),
        (cm, meter, 1E2),
        (mm, meter, 1E3),
        (μm, meter, 1E6),
        (nm, meter, 1E9),
        (mile, au, 92955807.3),
        (km, ausi, 149597870.7),
        (km, ly, 9460730472580.8),
        (ly, pc, 3.26)))
      case _ => Nil
    })

  lazy val foot = byName(cgDR, "foot")
  lazy val ft = foot
  lazy val mile = byName(cgDR, "mile")
  lazy val meter = byName(cgDR, "meter")
  lazy val kilometer = byName(cgDR, "kilometer")
  lazy val km = kilometer
  lazy val centimeter = byName(cgDR, "centimeter")
  lazy val cm = centimeter
  lazy val millimeter = byName(cgDR, "millimeter")
  lazy val mm = millimeter
  lazy val micrometer = byName(cgDR, "micrometer")
  lazy val μm = micrometer
  lazy val nanometer = byName(cgDR, "nanometer")
  lazy val nm = nanometer
  lazy val au = byName(cgDR, "Astronomical Unit")
  lazy val auSI = byName(cgDR, "Astronomical Unit (SI)")
  lazy val lightyear = byName(cgDR, "light year")
  lazy val parsec = byName(cgDR, "parsec")

  implicit val mdr = modulize[Distance, Rational]
  
  lazy val ny2LA = Rational("2443.79") *: mile // Some("NY to LA"), None, Some("http://www.mapcrow.info/Distance_between_New_York_US_and_Los_Angeles_US.html"))
  lazy val milkyWayDiameter = Rational(100000) *: lightyear // Some("Milky Way Diameter"), None, Some("http://en.wikipedia.org/wiki/Milky_Way"))
  lazy val toAndromeda = Rational(2.6E6) *: lightyear // Some("Distance to Andromeda"), None, Some("http://en.wikipedia.org/wiki/Andromeda_Galaxy")))))))))))))
  
}
