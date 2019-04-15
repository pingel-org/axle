package axle.stats

import spire.algebra.Field

object P {

  def apply[M[_, _], V, A](model: M[A, V], a: A)(implicit prob: ProbabilityModel[M], fieldV: Field[V]): () => V =
    () => prob.probabilityOf(model)(a)

}
