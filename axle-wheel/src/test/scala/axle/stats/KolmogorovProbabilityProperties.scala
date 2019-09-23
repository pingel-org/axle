package axle.stats

import org.scalacheck.Arbitrary
import org.scalacheck.Properties

import cats.Order
import cats.kernel.Eq

import spire.algebra.Field
import axle.algebra.Region

abstract class KolmogorovProbabilityProperties[M[_, _]: ProbabilityModel, E: Eq, V: Field: Order](
    name: String, arbModel: Arbitrary[M[E, V]], arbRegion: Arbitrary[Region[E]])
    extends Properties("Probability Model (Kolmogorov's Axioms)") {
  
      implicit val am = arbModel
      implicit val ar = arbRegion
  
      property(s"$name Basic measure: probabilities are non-negative") =
        KolmogorovProbabilityAxioms.basicMeasure
    
      property(s"$name Unit Measure: probabilities sum to one") =
        KolmogorovProbabilityAxioms.unitMeasure
    
      property(s"$name Combination: mutually exclusive events imply that probability of either is sum of probability of each") =
        KolmogorovProbabilityAxioms.combination(ProbabilityModel[M], Eq[E], Field[V], Order[V], arbModel, arbRegion)
    }
  