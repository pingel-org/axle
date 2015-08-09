package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import axle.algebra.Scale
import axle.algebra.Scale10s
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

  lazy val planck = unit("Planck length", "ℓP", Some("http://en.wikipedia.org/wiki/Planck_length"))
  lazy val ℓP = planck

  def units: List[UnitOfMeasurement[Distance]] =
    List(foot, mile, meter, kilometer, centimeter, millimeter, micrometer, nanometer,
      astronomicalUnit, astronomicalUnitSI, lightyear, parsec, planck)

}

trait DistanceConverter[N] extends UnitConverter[Distance, N] with DistanceUnits {

  def defaultUnit = meter
}

object Distance {

  import spire.algebra.Module
  import spire.math._
  import spire.implicits._

  def converterGraph[N: Field: Eq, DG](
    implicit module: Module[N, Double], moduleRational: Module[N, Rational],
    evDG: DirectedGraph[DG, UnitOfMeasurement[Distance], N => N]) =
    new UnitConverterGraph[Distance, N, DG] with DistanceConverter[N] {

      def links: Seq[(UnitOfMeasurement[Distance], UnitOfMeasurement[Distance], Bijection[N, N])] =
        List[(UnitOfMeasurement[Distance], UnitOfMeasurement[Distance], Bijection[N, N])](
          (foot, mile, Scale(Rational(5280))),
          (foot, meter, Scale(3.2808398950131235)),
          (kilometer, mile, Scale(1.609344)),
          (lightyear, parsec, Scale(3.26)),
          (nm, meter, Scale10s(9)),
          (μm, meter, Scale10s(6)),
          (millimeter, meter, Scale10s(3)),
          (centimeter, meter, Scale10s(2)),
          (meter, kilometer, Scale10s(3)),
          (mile, au, Scale(92955807.3)),
          (km, auSI, Scale(149597870.7)),
          (km, ly, Scale(9460730472580.8)),
          (planck, nm, Scale(16.162e-27)))

    }

}