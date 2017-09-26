package axle.stats

//sealed trait PExpr[N] extends Function0[N] {
//
//   def *[R](right: R) = PMultiply(this, right)
//}

case class P(c: CaseExpr) {

  def value[N, A](implicit prob: Probability[Variable[A], A, N]): N = ???
}

// case class PMultiply[L, R, N](left: L, right: R) extends PExpr[N]
