package axle.algebra

import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.specs2.mutable.Specification
import org.typelevel.discipline.Predicate
import org.typelevel.discipline.specs2.mutable.Discipline
import spire.implicits.DoubleAlgebra
import spire.implicits.IntAlgebra
import spire.laws.VectorSpaceLaws
import axle.algebra.GeoCoordinates.geoCoordinatesMetricSpace
// import axle.algebra.modules.doubleDoubleModule
import axle.distanceOnSphere
import axle.jung.directedGraphJung
import axle.quanta._
import axle.quanta.Angle
import axle.quanta.UnitOfMeasurement
import axle.quanta.UnittedQuantity
import edu.uci.ics.jung.graph.DirectedSparseGraph
import spire.implicits.DoubleAlgebra
import spire.implicits.metricSpaceOps
import spire.implicits.moduleOps

class GeoMetricSpaceSpec
    extends Specification
    with Discipline {

  implicit val angleConverter = {
    import axle.algebra.modules.doubleRationalModule
    Angle.converterGraph[Double, DirectedSparseGraph[UnitOfMeasurement[Angle], Double => Double]]
  }
  import angleConverter.°

  implicit val space = geoCoordinatesMetricSpace[Double]

  implicit val genAngleMagnitudeDouble = Gen.choose[Double](-180d, 180d)

  def genAngle: Gen[UnittedQuantity[Angle, Double]] = for {
    magnitude <- genAngleMagnitudeDouble
  } yield magnitude *: °

  def genCoords: Gen[GeoCoordinates[Double]] = for {
    lat <- genAngle
    long <- genAngle
  } yield GeoCoordinates(lat, long)

  implicit def arbCoords: Arbitrary[GeoCoordinates[Double]] =
    Arbitrary(genCoords)

  implicit def arbAngle: Arbitrary[UnittedQuantity[Angle, Double]] =
    Arbitrary(genAngle)

  implicit val pred: Predicate[UnittedQuantity[Angle, Double]] =
    new Predicate[UnittedQuantity[Angle, Double]] {
      def apply(a: UnittedQuantity[Angle, Double]) = true
    }

  import spire.implicits.DoubleAlgebra
  val ag = axle.quanta.quantumAdditiveGroup[Angle, Double]

  // The metric space laws require a Rng, even though the times method is not called.
  // A future version of spire could relax the constraint.
  // For now this Rng is created with an unimplemented times method
  //  ... in order for the tests to pass
  import spire.algebra.Rng
  implicit val angleDoubleRng: Rng[UnittedQuantity[Angle, Double]] =
    new Rng[UnittedQuantity[Angle, Double]] {

      def negate(x: UnittedQuantity[Angle, Double]): UnittedQuantity[Angle, Double] =
        ag.negate(x)

      def zero: UnittedQuantity[Angle, Double] =
        ag.zero

      def plus(x: UnittedQuantity[Angle, Double], y: UnittedQuantity[Angle, Double]): UnittedQuantity[Angle, Double] =
        ag.plus(x, y)

      def times(x: UnittedQuantity[Angle, Double], y: UnittedQuantity[Angle, Double]): UnittedQuantity[Angle, Double] =
        ???
    }

  //  checkAll(s"GeoCoordinates metric space",
  //    VectorSpaceLaws[GeoCoordinates[Double], UnittedQuantity[Angle, Double]].metricSpace)

  // Note: Currently failing "space.symmetric"
  // A counter-example is: 
  val p1 = GeoCoordinates(-45.78882683235767 *: °, 168.23386137273712 *: °)
  val p2 = GeoCoordinates(-20.06087425414168 *: °, -94.44662683269094 *: °)
  // This would likely be fixed by testing Real values and using
  // Taylor-series approximations for the trig functions

}