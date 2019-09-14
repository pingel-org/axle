package axle.stats

import cats.kernel.Eq
import spire.algebra.Field
import axle.algebra.RegionEq

object P {

  def apply[M[_, _], V, A](model: M[A, V], a: A)(implicit prob: ProbabilityModel[M], fieldV: Field[V], eqA: Eq[A]): () => V =
    () => prob.probabilityOf(model)(RegionEq(a))

}
