package axle.stats

import spire.algebra.Field

case class PMultiply[N: Field](left: Probability[N], right: () => N) extends Probability[N] {

  val field = implicitly[Field[N]]
  
  def apply(): N = field.times(left(), right())

  def bayes: () => N = ???
}
