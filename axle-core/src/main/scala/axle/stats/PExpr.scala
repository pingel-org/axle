package axle.stats

//sealed trait PExpr[N] extends Function0[N] {
//
//   def *[R](right: R) = PMultiply(this, right)
//}

object P {

  def apply[M, N, A](model: M, c: CaseIs[A])(implicit prob: Probability[M, A, N]): () => N =
    () => prob.apply(model, c)

  def apply[M, N, A, G](model: M, c: CaseGiven[A, G])(implicit prob: ProbabilityGiven1[M, A, G, N]): () => N =
    () => prob.apply(model, c)

}

// case class PMultiply[L, R, N](left: L, right: R) extends PExpr[N]
