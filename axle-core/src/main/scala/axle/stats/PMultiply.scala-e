package axle.stats

import spire.algebra.Field

case class PMultiply[N: Field](left: Probability[N], right: () => N) extends Probability[N] {
  
  def apply(): N = Field[N].times(left(), right())

  def bayes: () => N = ???
}
