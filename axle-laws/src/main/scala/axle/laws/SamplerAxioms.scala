package axle.laws

import org.scalacheck.Arbitrary
import org.scalacheck.Prop
import org.scalacheck.Prop.forAll

import cats.kernel.Eq
import cats.implicits._

import spire.algebra._
import spire.random._
import spire.math.sqrt
import spire.implicits.additiveGroupOps

import axle.probability._
import axle.algebra._
import axle.math.square
import axle.stats.TallyDistribution
import axle.syntax.kolmogorov._
import axle.syntax.sampler._

object SamplerAxioms {

  def nonZero[
    T,
    M[_, _]: Kolmogorov: Sampler,
    E: Eq,
    V: Field: Dist: Order: Eq](
    arbT: Arbitrary[T],
    modelFn: T => M[E, V],
    arbRegionFn: T => Arbitrary[Region[E]],
    eqREFn: T => Eq[Region[E]]): Prop = {

      implicit val implicitArbT = arbT

      val v0 = Field[V].zero

      forAll { t: T =>
        val model: M[E, V] = modelFn(t)
        forAll { seed: Int =>
          val rng = Random.generatorFromSeed(Seed(seed))
          model.P(RegionEq(model.sample(rng))) > v0
        }
      }
    }

  def distance[
    L[_, _]: Kolmogorov,
    R[_, _]: Kolmogorov,
    E: Eq,
    V: Field: Dist: Order: Eq: NRoot](
    domain: Iterable[E],
    left: L[E, V],
    right: R[E, V]): V =
    sqrt(
      domain
      .map( x => square(left.P(RegionEq(x)) - right.P(RegionEq(x))) )
      .foldLeft(Field[V].zero)(Field[V].plus _)
    )

  def convergence[
    T,
    M[_, _]: Kolmogorov: Sampler,
    E: Eq,
    V: Field: Dist: Order: Eq: NRoot](
    arbT: Arbitrary[T],
    modelFn: T => M[E, V],
    domain: T => Iterable[E],
    few: T => Int,
    many: T => Int,
    arbRegionFn: T => Arbitrary[Region[E]],
    eqREFn: T => Eq[Region[E]]): Prop = {

      import axle.syntax.talliable._
      implicit val implicitArbT = arbT

      forAll { t: T =>
        val model: M[E, V] = modelFn(t)
        forAll { seed: Int =>
          val rng = Random.generatorFromSeed(Seed(seed))

          val fewTally = TallyDistribution((1 to few(t)).map { _ => model.sample(rng) } tally )

          val manyTally = TallyDistribution((1 to many(t)).map { _ => model.sample(rng) } tally )

          distance[TallyDistribution, M, E, V](domain(t), fewTally, model) >= distance[TallyDistribution, M, E, V](domain(t), manyTally, model)
        }
      }
    }

}
