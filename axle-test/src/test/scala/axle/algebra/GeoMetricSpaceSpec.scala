package axle.algebra

import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.specs2.mutable.Specification
import org.typelevel.discipline.Predicate
import org.typelevel.discipline.specs2.mutable.Discipline
import spire.implicits.DoubleAlgebra
import spire.implicits.IntAlgebra
import spire.math.Real
import spire.laws.VectorSpaceLaws
import axle.algebra.GeoCoordinates.geoCoordinatesMetricSpace
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

  import spire.implicits._
  
  implicit val angleConverter: AngleConverter[Real] = {
    import axle.algebra.modules.realRationalModule
    import axle.algebra.modules.realDoubleModule
    Angle.converterGraph[Real, DirectedSparseGraph[UnitOfMeasurement[Angle], Real => Real]]
  }
  import angleConverter.°

  implicit val space = geoCoordinatesMetricSpace[Real]

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

  import spire.math.RealAlgebra
  val ag = axle.quanta.quantumAdditiveGroup[Angle, Real]

  // The metric space laws require a Rng, even though the times method is not called.
  // A future version of spire could relax the constraint.
  // For now this Rng is created with an unimplemented times method
  //  ... in order for the tests to pass
  import spire.algebra.Rng
  implicit val angleRealRng: Rng[UnittedQuantity[Angle, Real]] =
    new Rng[UnittedQuantity[Angle, Real]] {

      def negate(x: UnittedQuantity[Angle, Real]): UnittedQuantity[Angle, Real] =
        ag.negate(x)

      def zero: UnittedQuantity[Angle, Real] =
        ag.zero

      def plus(x: UnittedQuantity[Angle, Real], y: UnittedQuantity[Angle, Real]): UnittedQuantity[Angle, Real] =
        ag.plus(x, y)

      def times(x: UnittedQuantity[Angle, Real], y: UnittedQuantity[Angle, Real]): UnittedQuantity[Angle, Real] =
        ???
    }

  checkAll(s"GeoCoordinates metric space",
    VectorSpaceLaws[GeoCoordinates[Real], UnittedQuantity[Angle, Real]].metricSpace)

  // Note: Currently failing "space.symmetric"
  // A counter-example is: 
  // val p1 = GeoCoordinates(-45.78882683235767 *: °, 168.23386137273712 *: °)
  // val p2 = GeoCoordinates(-20.06087425414168 *: °, -94.44662683269094 *: °)
  // This would likely be fixed by testing Real values and using
  // Taylor-series approximations for the trig functions

}