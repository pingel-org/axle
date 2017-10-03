package axle.stats

object P {

  def apply[M[_], N, A](model: M[A], a: A)(implicit prob: Probability[M, A, N]): () => N =
    () => prob.probabilityOf(model, a)

}
