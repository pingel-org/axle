package axle.stats

import axle._

case class PMultiply(left: Probability, right: Double) extends Probability {

  def apply() = left() * right

  def bayes() = ???
}
