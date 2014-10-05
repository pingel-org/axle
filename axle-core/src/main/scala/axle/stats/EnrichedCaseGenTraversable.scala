package axle.stats

import scala.collection.GenTraversable

import spire.algebra.Field

// Note: Actually enriching an Iterable (TODO fix)

case class EnrichedCaseGenTraversable[A: Manifest, N: Field](cgt: Iterable[Case[A, N]]) {
  def ∧(): Case[List[A], N] = CaseAndGT(cgt)
}
