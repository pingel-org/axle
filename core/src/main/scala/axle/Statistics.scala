package axle

object Statistics {

  import collection._
  import axle.stats._
  import axle.Enrichments._
  // import shapeless._

  implicit def rv2it[K](rv: RandomVariable[K]) = rv.getValues.get

  case class EnrichedCaseGenTraversable[A](cgt: GenTraversable[Case[A]]) {
    def ∧(): Case[List[A]] = CaseAndGT(cgt)
  }

  implicit def enrichCaseGenTraversable[A](cgt: GenTraversable[Case[A]]) = EnrichedCaseGenTraversable(cgt)

  case class P[A](c: Case[A]) extends Function0[Double] {
    def *(right: Double) = PMultiply(this, () => right)
    def *(right: () => Double) = PMultiply(this, right)
    def apply(): Double = c.probability()
  }

  case class PMultiply[A](left: P[A], right: () => Double) extends Function0[Double] {
    def apply() = left() * right()
  }

  def bayes[A](p: P[A]): () => Double = p.c match {
    // TODO: also check that "left" and "right" have no "given"
    case CaseAnd(left, right) => P(left | right) * bayes(P(right))
    case _ => P(p.c)
  }

  def coin(pHead: Double = 0.5) = RandomVariable0("coin",
    values = Some(immutable.Set('HEAD, 'TAIL)),
    distribution = Some(new ConditionalProbabilityTable0(immutable.Map('HEAD -> pHead, 'TAIL -> (1.0 - pHead)))))

}