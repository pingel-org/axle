package axle.stats

import org.scalacheck.Arbitrary
import org.scalacheck.Properties

import cats.Order
import cats.kernel.Eq

import spire.algebra.Field
import axle.algebra.Region

abstract class BayesTheoremProperty[T, M[_, _]: ProbabilityModel, E: Eq, V: Field: Order](
    name: String,
    arbT: Arbitrary[T],
    modelFn: T => M[E, V],
    arbRegionFn: T => Arbitrary[Region[E]],
    eqREFn: T => Eq[Region[E]])
    extends Properties("Probability Model (Bayes Theorem)") {
  
      property(s"$name Bayes Theorem: P(A|B) * P(B) = P(B|A) * P(A)") =
        BayesTheoremAxiom.axiom(arbT, modelFn, arbRegionFn, eqREFn)

}
