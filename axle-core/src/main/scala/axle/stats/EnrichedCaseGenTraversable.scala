package axle.stats

import collection.GenTraversable

case class EnrichedCaseGenTraversable[A: Manifest](cgt: GenTraversable[Case[A]]) {
  def ∧(): Case[List[A]] = CaseAndGT(cgt)
}
