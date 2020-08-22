package axle.stats

import org.scalacheck.Arbitrary
import org.scalacheck.Prop
import org.scalacheck.Prop.forAll

import cats.kernel.Eq

import spire.algebra.Field

import axle.algebra.Region
import axle.syntax.probabilitymodel._

object BayesTheoremAxiom {

  import cats.syntax.order._

  /**
   *
   * P(A|B) = P(B|A) * P(A) / P(B)
   * 
   */

  def axiom[T, M[_, _]: ProbabilityModel, E: Eq, V: Field: Eq](
    arbT: Arbitrary[T],
    modelFn: T => M[E, V],
    arbRegionFn: T => Arbitrary[Region[E]],
    eqREFn: T => Eq[Region[E]]): Prop = {

      implicit val implicitArbT = arbT
      import spire.implicits._

      forAll { t: T =>
        val model: M[E, V] = modelFn(t)
        implicit val arbRegion = arbRegionFn(t)
        forAll { (a: Region[E], b: Region[E]) =>
          (model.P(b) === Field[V].zero) ||
            model.filter(b).P(a) === ( model.filter(a).P(b) * model.P(a) / model.P(b))
        }
      }
    }

}
