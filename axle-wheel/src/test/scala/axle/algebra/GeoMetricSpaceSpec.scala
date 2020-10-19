package axle.algebra

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers
import org.scalacheck.Arbitrary
import org.typelevel.discipline.Predicate
import org.typelevel.discipline.scalatest.Discipline

import edu.uci.ics.jung.graph.DirectedSparseGraph

import spire.math.Real
import spire.laws.VectorSpaceLaws

import axle.jung.directedGraphJung
import axle.quanta._
import axle.quanta.Angle
import axle.quanta.UnittedQuantity
import axle.laws.generator._

class GeoMetricSpaceSpec
  extends AnyFunSuite
  with Matchers
  with Discipline {

  implicit val angleConverter: AngleConverter[Real] = {
    import axle.algebra.modules.realRationalModule
    Angle.converterGraphK2[Real, DirectedSparseGraph]
  }

  //  implicit val space = {
  //    import axle.spireToCatsEq
  //    geoCoordinatesMetricSpace[Real]
  //  }

  val ag = axle.quanta.quantumAdditiveGroup[Angle, Real]

  implicit val eqgcr = cats.kernel.Eq[GeoCoordinates[Real]]

  implicit val arbCoords: Arbitrary[GeoCoordinates[Real]] =
    Arbitrary(genCoords)

  implicit val ova = axle.quanta.UnittedQuantity.orderUQ[Angle, Real]

  implicit val equaqr = axle.quanta.UnittedQuantity.eqqqn[Angle, Real]

  implicit val arbAngle: Arbitrary[UnittedQuantity[Angle, Real]] =
    Arbitrary(genAngle)

  implicit val pred: Predicate[UnittedQuantity[Angle, Real]] = Predicate.const[UnittedQuantity[Angle, Real]](true)

  val vsl = VectorSpaceLaws[GeoCoordinates[Real], UnittedQuantity[Angle, Real]](
    eqgcr, arbCoords, equaqr, arbAngle, pred)

  implicit val msva: spire.algebra.MetricSpace[GeoCoordinates[Real], UnittedQuantity[Angle, Real]] =
    GeoCoordinates.geoCoordinatesMetricSpace[Real]

  implicit val ama: spire.algebra.AdditiveMonoid[UnittedQuantity[Angle, Real]] =
    axle.quanta.quantumAdditiveGroup[Angle, Real]

  checkAll(s"GeoCoordinates metric space", vsl.metricSpace(msva, ova, ama))

}
