package axle.quanta

import axle.graph.DirectedGraph
import axle.graph.Vertex
import spire.algebra.Field
import spire.algebra.Eq
import spire.implicits.moduleOps
import spire.math.Real
import spire.math.Rational
import spire.math.Rational.apply
import axle.graph.DirectedGraph
import axle.graph.Vertex
import spire.algebra.Field
import spire.algebra.Eq
import spire.math.Rational
import spire.math.Real
import spire.implicits.eqOps
import spire.implicits.moduleOps
import spire.implicits.groupOps
import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.implicits.additiveGroupOps
import spire.implicits.additiveSemigroupOps
import spire.implicits._

abstract class Distance extends Quantum {
  def wikipediaUrl = "http://en.wikipedia.org/wiki/Orders_of_magnitude_(length)"
}

object Distance extends Distance {

  type Q = Distance

  def units[N: Field: Eq] = List[UnitOfMeasurement[Q, N]](
    unit("foot", "ft"),
    unit("mile", "m", Some("http://en.wikipedia.org/wiki/Mile")),
    unit("meter", "m"),
    unit("kilometer", "km"),
    unit("centimeter", "cm"),
    unit("millimeter", "mm"),
    unit("micrometer", "μm"),
    unit("nanometer", "nm"),
    unit("Astronomical Unit", "AU", Some("http://en.wikipedia.org/wiki/Astronomical_unit")),
    unit("Astronomical Unit (SI)", "AU", Some("http://en.wikipedia.org/wiki/Astronomical_unit")),
    unit("lightyear", "ly", Some("http://en.wikipedia.org/wiki/Light-year")),
    unit("parsec", "pc", Some("http://en.wikipedia.org/wiki/Parsec")))

  def links[N: Field: Eq] = {
    implicit val baseCG = cgnDisconnected[N]
    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], N => N, N => N)](
      (foot, mile, _ * 5280, _ / 5280),
      (kilometer, mile, _ * 1.609344, _ / 1.609344),
      (meter, kilometer, _ * 1E3, _ / 1E3),
      (lightyear, parsec, _ * 3.26, _ / 3.26),
      (centimeter, meter, _ * 1E2, _ / 1E2),
      (millimeter, meter, _ * 1E3, _ / 1E3),
      (μm, meter, _ * 1E6, _ / 1E6),
      (nm, meter, _ * 1E9, _ / 1E9),
      (mile, au, _ * 92955807.3, _ / 92955807.3),
      (km, auSI, _ * 149597870.7, _ / 149597870.7),
      (km, ly, _ * 9460730472580.8, _ / 9460730472580.8))
  }
  
  def centimeter[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "centimeter")
  def cm[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "centimeter")
  def meter[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "meter")
  def kilometer[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "kilometer")
  def km[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "kilometer")
  def foot[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "foot")
  def ft[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "foot")
  def mile[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "mile")
  def parsec[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "parsec")
  def lightyear[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "lightyear")
  def ly[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "lightyear")
  def millimeter[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "millimeter")
  def mm[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "millimeter")
  def micrometer[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "micrometer")
  def μm[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "micrometer")
  def nanometer[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "nanometer")
  def nm[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "nanometer")
  def au[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "Astronomical Unit")
  def auSI[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "Astronomical Unit (SI)")

  //  def ny2LA = Rational("2443.79") *: mile // Some("NY to LA"), None, Some("http://www.mapcrow.info/Distance_between_New_York_US_and_Los_Angeles_US.html"))
  //  lazy val milkyWayDiameter = Rational(100000) *: lightyear // Some("Milky Way Diameter"), None, Some("http://en.wikipedia.org/wiki/Milky_Way"))
  //  lazy val toAndromeda = Rational(2.6E6) *: lightyear // Some("Distance to Andromeda"), None, Some("http://en.wikipedia.org/wiki/Andromeda_Galaxy")))))))))))))

}
