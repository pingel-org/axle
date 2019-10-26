package axle.stats

import org.scalacheck.Arbitrary
import org.scalacheck.Properties

import cats.Order
import cats.kernel.Eq

import spire.algebra.Field
import axle.algebra.Region

abstract class KolmogorovProbabilityProperties[T, M[_, _]: ProbabilityModel, E: Eq, V: Field: Order](
    name: String,
    arbT: Arbitrary[T],
    modelFn: T => M[E, V],
    arbRegionFn: T => Arbitrary[Region[E]],
    eqREFn: T => Eq[Region[E]])
    extends Properties("Probability Model (Kolmogorov's Axioms)") {
  
      property(s"$name Basic measure: probabilities are non-negative") =
        KolmogorovProbabilityAxioms.basicMeasure(arbT, modelFn, arbRegionFn)

      property(s"$name Unit Measure: probabilities sum to one") =
        KolmogorovProbabilityAxioms.unitMeasure(arbT, modelFn)

      property(s"$name Combination: mutually exclusive events imply that probability of either is sum of probability of each") =
        KolmogorovProbabilityAxioms.combination(arbT, modelFn, arbRegionFn, eqREFn) //(ProbabilityModel[M], Eq[E], Field[V], Order[V])

}
