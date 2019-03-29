
package axle.stats

import org.scalacheck.Gen
import org.scalacheck.Arbitrary
import org.scalacheck.Properties
import org.scalacheck.Prop
import org.scalacheck.Prop.forAll

import cats.implicits._
import cats.Order

import axle.game.Dice._
import spire.math._
import spire.algebra.Field

object KolmogorovProbabilityAxioms {

  def basicMeasure[M[_, _]: ProbabilityModel, E, V: Field: Order](model: M[E, V]): Prop = {
    implicit val arbValue = Arbitrary(Gen.oneOf(ProbabilityModel[M].values(model)))
    forAll { (event: E) =>
      ProbabilityModel[M].probabilityOf(model, event) >= Field[V].zero
    }
  }
  
  def unitMeasure[M[_, _]: ProbabilityModel, E, V: Field: Order](model: M[E, V]): Prop =
    ProbabilityModel[M].values(model).map({ e: E => ProbabilityModel[M].probabilityOf(model, e) }).reduce(Field[V].plus) == Field[V].one
   
  def combination[M[_, _]: ProbabilityModel, E, V: Field: Order](model: M[E, V]): Prop = {
    implicit val arbValue = Arbitrary(Gen.oneOf(ProbabilityModel[M].values(model)))
    forAll { (a: E, b: E) =>
      // ??? // (a != b) => P(A or B) == P(A) + P(B)
      true
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
