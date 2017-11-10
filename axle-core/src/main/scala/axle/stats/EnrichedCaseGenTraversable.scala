package axle.stats

// Note: Actually enriching an Iterable (TODO fix)

case class EnrichedCaseGenTraversable[R, A, N](cgt: Iterable[CaseIs[A]])
// def ∧(): Case[List[A], N] = CaseAndGT(cgt)
