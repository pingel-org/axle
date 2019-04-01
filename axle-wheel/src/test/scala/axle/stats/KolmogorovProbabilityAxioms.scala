package axle.stats

import org.scalacheck.Gen
import org.scalacheck.Arbitrary

import org.scalacheck.Prop
import org.scalacheck.Prop.forAll

import cats.Order
import cats.kernel.Eq

import spire.algebra.Field
import spire.implicits.additiveSemigroupOps

import axle.syntax.probabilitymodel._

object KolmogorovProbabilityAxioms {

  import cats.implicits._

  def basicMeasure[M[_, _]: ProbabilityModel, E, V: Field: Order](implicit arbModel: Arbitrary[M[E, V]]): Prop = {
    forAll { (model: M[E, V]) => 
      implicit val arbEvent = Arbitrary(Gen.oneOf(model.values))
      forAll { (event: E) =>
        model.P(event) >= Field[V].zero
      }
    }
  }

  def unitMeasure[M[_, _]: ProbabilityModel, E, V: Field: Order](implicit arbModel: Arbitrary[M[E, V]]): Prop =
    forAll { (model: M[E, V]) =>
      model.values.map({ e: E => model.P(e) }).reduce(Field[V].plus) == Field[V].one
    }

  def combination[M[_, _]: ProbabilityModel, E: Eq, V: Field: Order](implicit arbModel: Arbitrary[M[E, V]]): Prop =
    forAll { (model: M[E, V]) =>

      implicit val arbitraryExpression: Arbitrary[E => Boolean] =
        Arbitrary(Gen.oneOf(ProbabilityModel[M].values(model)).map( e => (v: E) => v === e) )
  
      forAll { (f: E => Boolean, g: E => Boolean) =>
        (!(model.values.filter(e => f(e) && g(e)).size === 0)) || (model.P( e => f(e) || g(e) ) === model.P(f) + model.P(g))
      }
    }
}
