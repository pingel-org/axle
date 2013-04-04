package axle.stats

import collection._

case class EnrichedCaseGenTraversable[A: Manifest](cgt: GenTraversable[Case[A]]) {
  def ∧(): Case[List[A]] = CaseAndGT(cgt)
}
