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

  import cats.implicits._

  // import org.scalacheck.Gen
  // implicit val arbEvent = Arbitrary(Gen.oneOf(model.values))
  // implicit val arbitraryExpression: Arbitrary[E => Boolean] = Arbitrary(Gen.oneOf(ProbabilityModel[M].values(model)).map( e => (v: E) => v === e) )

  def basicMeasure[M[_, _]: ProbabilityModel, E, V: Field: Order](
    implicit arbModel: Arbitrary[M[E, V]], arbRegion: Arbitrary[Region[E]]): Prop = {
    forAll { (model: M[E, V]) => 
      forAll { (region: Region[E]) =>
        model.P(region) >= Field[V].zero
      }
    }
  }

  def unitMeasure[M[_, _]: ProbabilityModel, E, V: Field: Order](
    implicit arbModel: Arbitrary[M[E, V]]): Prop =
    forAll { (model: M[E, V]) =>
      model.P(RegionAll()) === Field[V].one
    }

  def combination[M[_, _]: ProbabilityModel, E: Eq, V: Field: Order](
    implicit arbModel: Arbitrary[M[E, V]], arbRegion: Arbitrary[Region[E]]): Prop =
    forAll { (model: M[E, V], e1: Region[E], e2: Region[E]) =>
      (!((e1 and e2) === RegionEmpty() )) || (model.P(e1 or e2) === model.P(e1) + model.P(e2))
    }
}
