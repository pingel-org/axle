package axle.stats

import collection.GenTraversable
import spire.algebra._
import spire.math._

case class EnrichedCaseGenTraversable[A: Manifest, N: Field](cgt: GenTraversable[Case[A, N]]) {
  def ∧(): Case[List[A], N] = CaseAndGT(cgt)
}
