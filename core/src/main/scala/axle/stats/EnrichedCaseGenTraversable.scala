package axle.stats

import collection._

case class EnrichedCaseGenTraversable[A](cgt: GenTraversable[Case[A]]) {
  def ∧(): Case[List[A]] = CaseAndGT(cgt)
}
