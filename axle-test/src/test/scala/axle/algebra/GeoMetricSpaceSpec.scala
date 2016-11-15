package axle.algebra

import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.specs2.mutable.Specification
import org.typelevel.discipline.Predicate
import org.typelevel.discipline.specs2.mutable.Discipline
import spire.math.Real
import spire.laws.VectorSpaceLaws
import axle.algebra.GeoCoordinates.geoCoordinatesMetricSpace
import axle.jung.directedGraphJung
import axle.quanta._
import axle.quanta.Angle
import axle.quanta.UnittedQuantity
import edu.uci.ics.jung.graph.DirectedSparseGraph

class GeoMetricSpaceSpec
    extends Specification
    with Discipline {

  implicit val angleConverter: AngleConverter[Real] = {
    import axle.algebra.modules.realRationalModule
    import axle.algebra.modules.realDoubleModule
    import axle.spireToCatsEq
    import axle.catsToSpireOrder
    Angle.converterGraphK2[Real, DirectedSparseGraph]
  }
  import angleConverter.°

  implicit val space = {
    import axle.spireToCatsEq
    geoCoordinatesMetricSpace[Real]
  }

  implicit val genAngleMagnitudeDouble: Gen[Double] = Gen.choose[Double](-180d, 180d)

  def genAngle: Gen[UnittedQuantity[Angle, Real]] = for {
    magnitude <- genAngleMagnitudeDouble
  } yield Real(magnitude) *: °

  def genCoords: Gen[GeoCoordinates[Real]] = for {
    lat <- genAngle
    long <- genAngle
  } yield GeoCoordinates(lat, long)

  implicit def arbCoords: Arbitrary[GeoCoordinates[Real]] =
    Arbitrary(genCoords)

  implicit def arbAngle: Arbitrary[UnittedQuantity[Angle, Real]] =
    Arbitrary(genAngle)

  implicit val pred: Predicate[UnittedQuantity[Angle, Real]] =
    new Predicate[UnittedQuantity[Angle, Real]] {
      def apply(a: UnittedQuantity[Angle, Real]) = true
    }

  val ag = axle.quanta.quantumAdditiveGroup[Angle, Real]

  import axle.catsToSpireEq
  val vsl = VectorSpaceLaws[GeoCoordinates[Real], UnittedQuantity[Angle, Real]]

  checkAll(s"GeoCoordinates metric space", vsl.metricSpace)

  // Note: Currently failing "space.symmetric"
  // A counter-example is: 
  // val p1 = GeoCoordinates(-45.78882683235767 *: °, 168.23386137273712 *: °)
  // val p2 = GeoCoordinates(-20.06087425414168 *: °, -94.44662683269094 *: °)
  // This would likely be fixed by testing Real values and using
  // Taylor-series approximations for the trig functions

}
