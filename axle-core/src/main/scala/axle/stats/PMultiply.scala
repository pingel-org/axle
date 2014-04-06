package axle.stats

import axle._
import spire.math._
import spire.algebra._

case class PMultiply[N: Field](left: Probability[N], right: () => N) extends Probability[N] {

  val field = implicitly[Field[N]]
  
  def apply(): N = field.times(left(), right())

  def bayes: () => N = ???
}
