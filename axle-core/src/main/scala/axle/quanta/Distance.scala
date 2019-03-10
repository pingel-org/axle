package axle.quanta

import cats.kernel.Eq

import spire.algebra.Field

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import axle.algebra.Scale
import axle.algebra.Scale10s

case class Distance() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Orders_of_magnitude_(length)"

}

trait DistanceUnits extends QuantumUnits[Distance] {

  lazy val inch = unit("inch", "in")
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
  lazy val lightyear = unit("lightyear", "ly", Some("http://en.wikipedia.org/wiki/Light-year"))
  lazy val ly = lightyear
  lazy val parsec = unit("parsec", "pc", Some("http://en.wikipedia.org/wiki/Parsec"))

  lazy val planck = unit("Planck length", "ℓP", Some("http://en.wikipedia.org/wiki/Planck_length"))
  lazy val ℓP = planck

  def units: List[UnitOfMeasurement[Distance]] =
    List(inch, foot, mile, meter, kilometer, centimeter, millimeter, micrometer, nanometer,
      astronomicalUnit, lightyear, parsec, planck)

}

trait DistanceConverter[N] extends UnitConverter[Distance, N] with DistanceUnits {

  def defaultUnit = meter
}

object Distance {

  import spire.algebra.Module
  import spire.math._

  def converterGraphK2[N: Field: Eq: ConvertableTo, DG[_, _]](
    implicit
    moduleRational: Module[N, Rational],
    evDG:           DirectedGraph[DG[UnitOfMeasurement[Distance], N => N], UnitOfMeasurement[Distance], N => N]) =
    converterGraph[N, DG[UnitOfMeasurement[Distance], N => N]]

  def converterGraph[N: Field: Eq: ConvertableTo, DG](
    implicit
    moduleRational: Module[N, Rational],
    evDG:           DirectedGraph[DG, UnitOfMeasurement[Distance], N => N]) =
    new UnitConverterGraph[Distance, N, DG] with DistanceConverter[N] {

      def links: Seq[(UnitOfMeasurement[Distance], UnitOfMeasurement[Distance], Bijection[N, N])] =
        List[(UnitOfMeasurement[Distance], UnitOfMeasurement[Distance], Bijection[N, N])](
          (inch, foot, Scale(12)),
          (foot, mile, Scale(Rational(5280))),
          (foot, meter, Scale(Rational(3.2808398950131235))), // <- metric/English boundary
          //(kilometer, mile, Scale(Rational(1.609344))),
          (nm, meter, Scale10s(9)),
          (μm, meter, Scale10s(6)),
          (millimeter, meter, Scale10s(3)),
          (centimeter, meter, Scale10s(2)),
          (meter, kilometer, Scale10s(3)),
          (km, au, Scale(Rational(149597870.7))),
          (km, ly, Scale(Rational(9460730472580.8))),
          (lightyear, parsec, Scale(Rational(3.26))),
          (planck, nm, Scale(Rational(16.162e-27))))

    }

}
