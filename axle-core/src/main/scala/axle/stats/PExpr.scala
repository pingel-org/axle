package axle.stats

//sealed trait PExpr[N] extends Function0[N] {
//
//   def *[R](right: R) = PMultiply(this, right)
//}

object P {

  def apply[N, A](c: CaseIs[A])(implicit prob: Probability[Variable[A], A, N]): () => N =
    () => prob.apply(c.variable, c.value)
}

// case class PMultiply[L, R, N](left: L, right: R) extends PExpr[N]
