package axle.stats

import axle._
import spire.math._

case class PMultiply(left: Probability, right: Real) extends Probability {

  def apply() = left() * right

  def bayes() = ???
}
