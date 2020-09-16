package axle.laws

import org.scalacheck.Arbitrary
import org.scalacheck.Prop
import org.scalacheck.Prop.forAll

import cats.kernel.Eq
import cats.implicits._

import spire.algebra._
import spire.random._

import axle.probability._
import axle.algebra._
import axle.syntax.kolmogorov._
import axle.syntax.sampler._

object SamplerAxioms {

  def nonZero[
    T, M[_, _]: Kolmogorov: Sampler,
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

}
