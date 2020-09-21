package axle.laws

import org.scalacheck.Arbitrary
import org.scalacheck.Properties

import cats.Order
import cats.kernel.Eq

import spire.algebra._
import spire.random.Dist

import axle.algebra.Region
import axle.probability._

/**
 * Types
 * 
 * T seed type
 * M model type
 * E event type -- M[E, _]
 * V probability value type -- M[_, V]
 * 
 */

abstract class SamplerProperties[
  T,
  M[_, _]: Kolmogorov: Sampler,
  E: Eq,
  V: Field: Order: Dist: NRoot](
    name: String,
    arbT: Arbitrary[T],
    modelFn: T => M[E, V],
    domain: T => Iterable[E],
    few: T => Int,
    many: T => Int,
    arbRegionFn: T => Arbitrary[Region[E]],
    eqREFn: T => Eq[Region[E]])
    extends Properties("Sampler Axioms") {
  
      property(s"$name Sampled values have non-zero probability") =
        SamplerAxioms.nonZero(arbT, modelFn, arbRegionFn, eqREFn)

      property(s"$name Sampled distribution converges to the underlying model") =
        SamplerAxioms.convergence(arbT, modelFn, domain, few, many, arbRegionFn, eqREFn)

}
