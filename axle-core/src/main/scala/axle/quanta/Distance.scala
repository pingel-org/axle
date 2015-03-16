package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Distance() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Orders_of_magnitude_(length)"

}

trait DistanceUnits extends QuantumUnits[Distance] {

  lazy val foot = unit("foot", "ft")
  lazy val ft = foot
  lazy val mile = unit("mile", "m", Some("http://en.wikipedia.org/wiki/Mile"))
  lazy val meter = unit("meter", "m")
  lazy val kilometer = unit("kilometer", "km")
  lazy val km = kilometer
  lazy val centimeter = unit("centimeter", "cm")
  lazy val cm = centimeter
  lazy val millimeter = unit("millimeter", "mm")
  lazy val mm = millimeter
  lazy val micrometer = unit("micrometer", "μm")
  lazy val μm = micrometer
  lazy val nanometer = unit("nanometer", "nm")
  lazy val nm = nanometer
  lazy val astronomicalUnit = unit("Astronomical Unit", "AU", Some("http://en.wikipedia.org/wiki/Astronomical_unit"))
  lazy val au = astronomicalUnit
  lazy val astronomicalUnitSI = unit("Astronomical Unit (SI)", "AU", Some("http://en.wikipedia.org/wiki/Astronomical_unit"))
  lazy val auSI = astronomicalUnitSI
  lazy val lightyear = unit("lightyear", "ly", Some("http://en.wikipedia.org/wiki/Light-year"))
  lazy val ly = lightyear
  lazy val parsec = unit("parsec", "pc", Some("http://en.wikipedia.org/wiki/Parsec"))

  def units: List[UnitOfMeasurement[Distance]] =
    List(foot, mile, meter, kilometer, centimeter, millimeter, micrometer, nanometer,
      astronomicalUnit, astronomicalUnitSI, lightyear, parsec)

}

trait DistanceConverter[N] extends UnitConverter[Distance, N] with DistanceUnits

object Distance {

  def converterGraph[N: Field: Eq, DG[_, _]: DirectedGraph] =
    new UnitConverterGraph[Distance, N, DG] with DistanceConverter[N] {

      def links: Seq[(UnitOfMeasurement[Distance], UnitOfMeasurement[Distance], Bijection[N, N])] =
        List[(UnitOfMeasurement[Distance], UnitOfMeasurement[Distance], Bijection[N, N])](
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
      //  //  def toAndromeda[N: Field: Eq](implicit cg: CG[N], module: Module[UnittedQuantity[Distance, N], N]) = Rational(2.6E6))))))))))) *: lightyear[N]

    }

}