package axle.algebra

import org.scalatest._
import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.typelevel.discipline.Predicate
import org.typelevel.discipline.scalatest.Discipline

import spire.math.Real
import spire.laws.VectorSpaceLaws
// import axle.algebra.GeoCoordinates.geoCoordinatesMetricSpace
import axle.jung.directedGraphJung
import axle.quanta._
import axle.quanta.Angle
import axle.quanta.UnittedQuantity
//import axle.eqReal
import axle.orderReal

import edu.uci.ics.jung.graph.DirectedSparseGraph
import cats.kernel.Eq

class GeoMetricSpaceSpec
    extends FunSuite with Matchers
    with Discipline {

  implicit val angleConverter: AngleConverter[Real] = {
    import axle.algebra.modules.realRationalModule
    import axle.algebra.modules.realDoubleModule
    Angle.converterGraphK2[Real, DirectedSparseGraph]
  }
  import angleConverter.°

  //  implicit val space = {
  //    import axle.spireToCatsEq
  //    geoCoordinatesMetricSpace[Real]
  //  }

  implicit val genAngleMagnitudeDouble: Gen[Double] = Gen.choose[Double](-180d, 180d)

  def genAngle: Gen[UnittedQuantity[Angle, Real]] = for {
    magnitude <- genAngleMagnitudeDouble
  } yield Real(magnitude) *: °

  def genCoords: Gen[GeoCoordinates[Real]] = for {
    lat <- genAngle
    long <- genAngle
  } yield GeoCoordinates(lat, long)

  val ag = axle.quanta.quantumAdditiveGroup[Angle, Real]

  // TODO spire conversion
  implicit val eqgcr: spire.algebra.Eq[GeoCoordinates[Real]] =
    new spire.algebra.Eq[GeoCoordinates[Real]] {
      val eqGeoReal = Eq[GeoCoordinates[Real]]
      def eqv(x: GeoCoordinates[Real], y: GeoCoordinates[Real]): Boolean =
        eqGeoReal.eqv(x, y)
    }

  implicit val arbCoords: Arbitrary[GeoCoordinates[Real]] =
    Arbitrary(genCoords)

  // TODO spire conversion
  implicit val ova: spire.algebra.Order[UnittedQuantity[Angle, Real]] =
    new spire.algebra.Order[UnittedQuantity[Angle, Real]] {
      val catsOrderUQAngleReal = cats.kernel.Order[UnittedQuantity[Angle, Real]]
      def compare(x: UnittedQuantity[Angle, Real], y: UnittedQuantity[Angle, Real]): Int =
        catsOrderUQAngleReal.compare(x, y)
    }

  // TODO spire conversion
  implicit val equaqr: spire.algebra.Eq[UnittedQuantity[Angle, Real]] =
    new spire.algebra.Eq[UnittedQuantity[Angle, Real]] {
      val eqUQAngleReal = Eq[UnittedQuantity[Angle, Real]]
      def eqv(x: UnittedQuantity[Angle, Real], y: UnittedQuantity[Angle, Real]): Boolean =
        eqUQAngleReal.eqv(x, y)
    }

  implicit val arbAngle: Arbitrary[UnittedQuantity[Angle, Real]] =
    Arbitrary(genAngle)

  implicit val pred: Predicate[UnittedQuantity[Angle, Real]] =
    new Predicate[UnittedQuantity[Angle, Real]] {
      def apply(a: UnittedQuantity[Angle, Real]) = true
    }

  val vsl = VectorSpaceLaws[GeoCoordinates[Real], UnittedQuantity[Angle, Real]](
    eqgcr, arbCoords, equaqr, arbAngle, pred)

  implicit val msva: spire.algebra.MetricSpace[GeoCoordinates[Real], UnittedQuantity[Angle, Real]] =
    spire.algebra.MetricSpace.apply[GeoCoordinates[Real], UnittedQuantity[Angle, Real]]

  implicit val ama: spire.algebra.AdditiveMonoid[UnittedQuantity[Angle, Real]] =
    axle.quanta.quantumAdditiveGroup[Angle, Real]

  // checkAll(s"GeoCoordinates metric space", vsl.metricSpace(msva, ova, ama))

  // Note: Currently failing "space.symmetric"
  // A counter-example is: 
  // val p1 = GeoCoordinates(-45.78882683235767 *: °, 168.23386137273712 *: °)
  // val p2 = GeoCoordinates(-20.06087425414168 *: °, -94.44662683269094 *: °)
  // This would likely be fixed by testing Real values and using
  // Taylor-series approximations for the trig functions

}
