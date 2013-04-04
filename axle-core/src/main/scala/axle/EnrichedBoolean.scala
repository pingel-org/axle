package axle

case class EnrichedBoolean(b: Boolean) {

  def ¬:() = !b

  def ∧(other: Boolean) = b && other
  def and(other: Boolean) = b && other

  def ∨(other: Boolean) = b || other
  def or(other: Boolean) = b || other

  def ⊃(other: Boolean) = (!b) || other
  def implies(other: Boolean) = (!b) || other

  def ⊕(other: Boolean) = ((!b) && (!other)) || (b && other)
  def ⊻(other: Boolean) = ((!b) && (!other)) || (b && other)
}
