package axle.stats

import scala.collection.GenTraversable

import spire.algebra.Field

case class EnrichedCaseGenTraversable[A: Manifest, N: Field](cgt: GenTraversable[Case[A, N]]) {
  def ∧(): Case[List[A], N] = CaseAndGT(cgt)
}
