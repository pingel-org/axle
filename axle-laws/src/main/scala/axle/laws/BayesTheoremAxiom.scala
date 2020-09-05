package axle.laws

import org.scalacheck.Arbitrary
import org.scalacheck.Prop
import org.scalacheck.Prop.forAll

import cats.kernel.Eq

import spire.algebra.Field

import axle.stats._
import axle.algebra.Region
import axle.syntax.probabilitymodel._

object BayesTheoremAxiom {

  import cats.syntax.order._

  /**
   *
   * P(A|B) * P(B) = P(B|A) * P(A)
   * 
   * aka P(A|B) = P(B|A) * P(A) / P(B), but without risk of division by zero
   * 
   */

  def axiom[T, M[_, _]: ProbabilityModel, E: Eq, V: Field: Eq](
    arbT: Arbitrary[T],
    modelFn: T => M[E, V],
    arbRegionFn: T => Arbitrary[Region[E]],
    eqREFn: T => Eq[Region[E]]): Prop = {

      implicit val implicitArbT = arbT
      import spire.implicits._

      val v0 = Field[V].zero

      forAll { t: T =>
        val model: M[E, V] = modelFn(t)
        implicit val arbRegion = arbRegionFn(t)
        forAll { (a: Region[E], b: Region[E]) =>
          (model.P(a) === v0 && model.filter(b).P(a) === v0) ||
          (model.P(b) === v0 && model.filter(a).P(b) === v0) ||
          (model.filter(b).P(a) * model.P(b)) === ( model.filter(a).P(b) * model.P(a) )
        }
      }
    }

}
