
package axle.stats

import org.scalacheck.Gen
import org.scalacheck.Arbitrary
import org.scalacheck.Properties
import org.scalacheck.Prop
import org.scalacheck.Prop.forAll

import cats.implicits._
import cats.Order
import cats.kernel.Eq

import spire.math._
import spire.algebra.Field
import spire.implicits.additiveSemigroupOps

import axle.game.Dice._
import axle.syntax.probabilitymodel._

object KolmogorovProbabilityAxioms {

  def basicMeasure[M[_, _]: ProbabilityModel, E, V: Field: Order](model: M[E, V]): Prop = {
    implicit val arbValue = Arbitrary(Gen.oneOf(model.values))
    forAll { (event: E) =>
      model.P(event) >= Field[V].zero
    }
  }
  
  def unitMeasure[M[_, _]: ProbabilityModel, E, V: Field: Order](model: M[E, V]): Prop =
    model.values.map({ e: E => model.P(e) }).reduce(Field[V].plus) == Field[V].one
   
  def combination[M[_, _]: ProbabilityModel, E: Eq, V: Field: Order](model: M[E, V]): Prop = {

    implicit val arbitraryExpression: Arbitrary[E => Boolean] =
      Arbitrary(Gen.oneOf(ProbabilityModel[M].values(model)).map( e => (v: E) => v === e) )

    forAll { (f: E => Boolean, g: E => Boolean) =>
      (!(model.values.filter(e => f(e) && g(e)).size === 0)) || (model.P( e => f(e) || g(e) ) === model.P(f) + model.P(g))
    }
  }
}
  
abstract class KolmogorovProbabilitySpec[M[_, _]: ProbabilityModel, E, V: Field: Order](name: String, model: M[E, V])
  extends Properties("Probability Model (Kolmogorov's Axioms)") {
  
  property(s"$name Basic measure: probabilities are non-negative") = KolmogorovProbabilityAxioms.basicMeasure(model)
  
  property(s"$name Unit Measure: probabilities sum to one") = KolmogorovProbabilityAxioms.unitMeasure(model)
  
  property(s"$name Combination: mutually exclusive events imply that probability of either is sum of probability of each") = KolmogorovProbabilityAxioms.combination(model)
}
  
class FairCoinIsKolmogorov extends KolmogorovProbabilitySpec("Fair coin", coin())
  
class BiasedCoinIsKolmogorov extends KolmogorovProbabilitySpec("2:1 biased coin coin", coin(Rational(1, 3)))
  
class D6IsKolmogorov extends KolmogorovProbabilitySpec("d6", die(6))
  
class TwoIndependentD6IsKolmogorov extends KolmogorovProbabilitySpec("2 independent d6", {
  type CPTR[T] = ConditionalProbabilityTable[T, Rational]
  import cats.syntax.all._
  for { a <- die(6): CPTR[Int]; b <- die(6): CPTR[Int]} yield a + b
})
