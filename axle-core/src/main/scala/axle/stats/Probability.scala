package axle.stats

// import spire.algebra.Field
// import spire.implicits.additiveGroupOps
// import axle.math.Π

trait Probability[M, A, N] {

  def apply(model: M, c: CaseIs[A]): N

  def values(model: M, variable: Variable[A]): IndexedSeq[A]

  def combine(variable: Variable[A], modelsToProbabilities: Map[M, N]): M
}

trait ProbabilityGiven1[M, A, G, N] extends Function2[M, CaseGiven[A, G], N] {

  def values(model: M, variable: Variable[A]): IndexedSeq[A]
}

trait ProbabilityGiven2[M, A, G1, G2, N]
extends Function2[M, CaseGiven2[A, G1, G2], N] {

  def values(model: M, variable: Variable[A]): IndexedSeq[A]
}

object Probability {

  // CaseAndGT (GenTraversable)
  //  def probability[B](given: Option[Case[B, N]] = None): N =
  //    given
  //      .map(g => Π[N, Iterable[N]](conjuncts map { (c: Case[A, N]) => P(c | g).apply() }))
  //      .getOrElse(Π[N, Iterable[N]](conjuncts map { P(_).apply() }))

  // CaseAnd
  //  def probability[C](given: Option[Case[C, N]] = None): N =
  //    (given.map(g => P(left | g) * P(right | g)).getOrElse(P(left) * P(right))).apply()

  // CaseOr
  //  def probability[C](given: Option[Case[C, N]] = None): N =
  //    given
  //      .map { g =>
  //        // P(left | g) + P(right | g) - P((left ∧ right) | g)
  //        field.plus(P(left | g).apply(), P(right | g).apply()) - P((left ∧ right) | g).apply()
  //      }
  //      .getOrElse(
  //        field.plus(P(left).apply(), P(right).apply()) - P(left ∧ right).apply())

  // CaseGiven
  //  def probability[C](givenArg: Option[Case[C, N]] = None): N = {
  //    assert(givenArg.isEmpty)
  //    c.probability(Some(given))
  //  }

  // CaseIs
  //  implicit def probabilityCaseIs[C, N]: Probability[CaseIs[C], N] =
  //    new Probability[CaseIs[C], N] {
  //      def apply(c: CaseIs[C], d: Distribution0[C, N]): N =
  //        d.probabilityOf(c.v)
  //    }

  //  implicit def probabilityCaseIsGiven[C, N]: N =
  //    new Probability[CaseIs[C], N] {
  //      def apply(): N = given
  //        .map(g => d1.asInstanceOf[Distribution1[A, G, N]].probabilityOf(v, g))
  //        .getOrElse(d1.probabilityOf(v))
  //    }

  // CaseIsnt
  // def probability[B](given: Option[Case[B, N]] = None): N = field.minus(field.one, P(distribution is v).apply())

}