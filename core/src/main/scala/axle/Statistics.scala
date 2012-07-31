package axle

object Statistics {

  import collection._
  import axle.stats._
  import axle.Enrichments._

  implicit def rv2it[K](rv: RandomVariable[K]) = rv.getValues.get

  case class EnrichedCaseGenTraversable[A](cgt: GenTraversable[Case[A]]) {
    def ∧(): Case[List[A]] = CaseAndGT(cgt)
  }

  implicit def enrichCaseGenTraversable[A](cgt: GenTraversable[Case[A]]) = EnrichedCaseGenTraversable(cgt)

  case class PMultiply(left: Probability, right: () => Double) extends Function0[Double] {
    def apply() = left() * right()
  }

  def coin(pHead: Double = 0.5) = RandomVariable0("coin",
    values = Some(List('HEAD, 'TAIL)),
    distribution = Some(new ConditionalProbabilityTable0(immutable.Map('HEAD -> pHead, 'TAIL -> (1.0 - pHead)))))

}