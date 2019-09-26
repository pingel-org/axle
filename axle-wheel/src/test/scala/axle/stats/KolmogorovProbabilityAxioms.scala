package axle.stats

import org.scalacheck.Arbitrary
import org.scalacheck.Prop
import org.scalacheck.Prop.forAll

import cats.Order
import cats.kernel.Eq

import spire.algebra.Field
import spire.implicits.additiveSemigroupOps

import axle.algebra.Region
import axle.algebra.RegionAll
import axle.algebra.RegionEmpty
import axle.syntax.probabilitymodel._

object KolmogorovProbabilityAxioms {

  import cats.syntax.order._

  // import org.scalacheck.Gen
  // implicit val arbEvent = Arbitrary(Gen.oneOf(model.values))
  // implicit val arbitraryExpression: Arbitrary[E => Boolean] = Arbitrary(Gen.oneOf(ProbabilityModel[M].values(model)).map( e => (v: E) => v === e) )

  def basicMeasure[T, M[_, _]: ProbabilityModel, E, V: Field: Order](
    arbT: Arbitrary[T],
    modelFn: T => M[E, V],
    arbRegionFn: T => Arbitrary[Region[E]]): Prop = {

    implicit val implicitArbT = arbT

    forAll { t: T => 

      val model: M[E, V] = modelFn(t)
      implicit val arbRegion = arbRegionFn(t)

      forAll { (region: Region[E]) =>
        model.P(region) >= Field[V].zero
      }
    }
  }

  def unitMeasure[T, M[_, _]: ProbabilityModel, E, V: Field: Order](
    arbT: Arbitrary[T],
    modelFn: T => M[E, V]): Prop = {

    implicit val implicitArbT = arbT

    forAll { t: T =>
      val model: M[E, V] = modelFn(t)
      model.P(RegionAll()) === Field[V].one
    }
  }

  def combination[T, M[_, _]: ProbabilityModel, E: Eq, V: Field: Order](
    arbT: Arbitrary[T],
    modelFn: T => M[E, V],
    arbRegionFn: T => Arbitrary[Region[E]],
    eqREFn: T => Eq[Region[E]]): Prop = {

      implicit val implicitArbT = arbT

      forAll { t: T =>
        val model: M[E, V] = modelFn(t)
        implicit val arbRegion = arbRegionFn(t)
        implicit val eqRE = eqREFn(t)
        forAll { (e1: Region[E], e2: Region[E]) =>
          (!((e1 and e2) === RegionEmpty() )) || (model.P(e1 or e2) === model.P(e1) + model.P(e2))
        }
      }
    }

}
