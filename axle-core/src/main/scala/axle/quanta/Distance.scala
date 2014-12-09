package axle.quanta

import axle.algebra.Vertex
import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field
import spire.math.Rational
import spire.math.Real

//class Distance[DG[_, _]: DirectedGraph] extends Quantum {
//
//  def wikipediaUrl = "http://en.wikipedia.org/wiki/Orders_of_magnitude_(length)"
//
//  type Q = this.type
//
//  def units[N: Field: Eq] = List[UnitOfMeasurement[Q, N]](
//    unit("foot", "ft"),
//    unit("mile", "m", Some("http://en.wikipedia.org/wiki/Mile")),
//    unit("meter", "m"),
//    unit("kilometer", "km"),
//    unit("centimeter", "cm"),
//    unit("millimeter", "mm"),
//    unit("micrometer", "μm"),
//    unit("nanometer", "nm"),
//    unit("Astronomical Unit", "AU", Some("http://en.wikipedia.org/wiki/Astronomical_unit")),
//    unit("Astronomical Unit (SI)", "AU", Some("http://en.wikipedia.org/wiki/Astronomical_unit")),
//    unit("lightyear", "ly", Some("http://en.wikipedia.org/wiki/Light-year")),
//    unit("parsec", "pc", Some("http://en.wikipedia.org/wiki/Parsec")))
//
//  def links[N: Field: Eq] = {
//    implicit val baseCG = cgnDisconnected[N, DG]
//    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])](
//      (foot, mile, ScaleInt(5280)),
//      (foot, meter, ScaleDouble(3.2808398950131235)),
//      (kilometer, mile, ScaleDouble(1.609344)),
//      (lightyear, parsec, ScaleDouble(3.26)),
//      (nm, meter, Scale10s(9)),
//      (μm, meter, Scale10s(6)),
//      (millimeter, meter, Scale10s(3)),
//      (centimeter, meter, Scale10s(2)),
//      (meter, kilometer, Scale10s(3)),
//      (mile, au, ScaleDouble(92955807.3)),
//      (km, auSI, ScaleDouble(149597870.7)),
//      (km, ly, ScaleDouble(9460730472580.8)))
//  }
//
//  def centimeter[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "centimeter")
//  def cm[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "centimeter")
//  def meter[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "meter")
//  def kilometer[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "kilometer")
//  def km[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "kilometer")
//  def foot[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "foot")
//  def ft[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "foot")
//  def mile[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "mile")
//  def parsec[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "parsec")
//  def lightyear[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "lightyear")
//  def ly[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "lightyear")
//  def millimeter[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "millimeter")
//  def mm[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "millimeter")
//  def micrometer[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "micrometer")
//  def μm[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "micrometer")
//  def nanometer[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "nanometer")
//  def nm[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "nanometer")
//  def au[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "Astronomical Unit")
//  def auSI[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "Astronomical Unit (SI)")
//
//  //  // http://www.mapcrow.info/Distance_between_New_York_US_and_Los_Angeles_US.html
//  //  def ny2LA[N: Field: Eq](implicit cg: CG[N], module: Module[UnittedQuantity[Distance, N], N]) = Rational("2443.79") *: mile[N]
//  //
//  //  // http://en.wikipedia.org/wiki/Milky_Way
//  //  def milkyWayDiameter[N: Field: Eq](implicit cg: CG[N], module: Module[UnittedQuantity[Distance, N], N]) = Rational(100000) *: lightyear[N]
//  //
//  //  // http://en.wikipedia.org/wiki/Andromeda_Galaxy
//  //  def toAndromeda[N: Field: Eq](implicit cg: CG[N], module: Module[UnittedQuantity[Distance, N], N]) = Rational(2.6E6) *: lightyear[N]
//
//}

case class Distance3() extends Quantum

object Distance3 extends Quantum {

  def unit[N: Field: Eq](name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement[Distance3, N](name, symbol, wiki)

  def foot[N: Field: Eq] = unit("foot", "ft")
  def ft[N: Field: Eq] = foot[N]
  def mile[N: Field: Eq] = unit("mile", "m", Some("http://en.wikipedia.org/wiki/Mile"))
  def meter[N: Field: Eq] = unit("meter", "m")
  def kilometer[N: Field: Eq] = unit("kilometer", "km")
  def km[N: Field: Eq] = kilometer[N]
  def centimeter[N: Field: Eq] = unit("centimeter", "cm")
  def cm[N: Field: Eq] = centimeter[N]
  def millimeter[N: Field: Eq] = unit("millimeter", "mm")
  def mm[N: Field: Eq] = millimeter[N]
  def micrometer[N: Field: Eq] = unit("micrometer", "μm")
  def μm[N: Field: Eq] = micrometer[N]
  def nanometer[N: Field: Eq] = unit("nanometer", "nm")
  def nm[N: Field: Eq] = nanometer[N]
  def astronomicalUnit[N: Field: Eq] = unit("Astronomical Unit", "AU", Some("http://en.wikipedia.org/wiki/Astronomical_unit"))
  def au[N: Field: Eq] = astronomicalUnit[N]
  def astronomicalUnitSI[N: Field: Eq] = unit("Astronomical Unit (SI)", "AU", Some("http://en.wikipedia.org/wiki/Astronomical_unit"))
  def auSI[N: Field: Eq] = astronomicalUnitSI[N]
  def lightyear[N: Field: Eq] = unit("lightyear", "ly", Some("http://en.wikipedia.org/wiki/Light-year"))
  def ly[N: Field: Eq] = lightyear[N]
  def parsec[N: Field: Eq] = unit("parsec", "pc", Some("http://en.wikipedia.org/wiki/Parsec"))

  def units[N: Field: Eq]: List[UnitOfMeasurement[Distance3, N]] =
    List(foot, mile, meter, kilometer, centimeter, millimeter, micrometer, nanometer,
      astronomicalUnit, astronomicalUnitSI, lightyear, parsec)

  def links[N: Field: Eq]: Seq[(UnitOfMeasurement[Distance3, N], UnitOfMeasurement[Distance3, N], Bijection[N, N])] =
    List[(UnitOfMeasurement[Distance3, N], UnitOfMeasurement[Distance3, N], Bijection[N, N])](
      (foot, mile, ScaleInt(5280)),
      (foot, meter, ScaleDouble(3.2808398950131235)),
      (kilometer, mile, ScaleDouble(1.609344)),
      (lightyear, parsec, ScaleDouble(3.26)),
      (nm, meter, Scale10s(9)),
      (μm, meter, Scale10s(6)),
      (millimeter, meter, Scale10s(3)),
      (centimeter, meter, Scale10s(2)),
      (meter, kilometer, Scale10s(3)),
      (mile, au, ScaleDouble(92955807.3)),
      (km, auSI, ScaleDouble(149597870.7)),
      (km, ly, ScaleDouble(9460730472580.8)))

  implicit def conversionGraph[N: Field: Eq, DG[_, _]: DirectedGraph] =
    Quantum.cgn(units, links)

}
