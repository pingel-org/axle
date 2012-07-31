package axle.stats

import axle.Statistics._

trait Probability extends Function0[Double] {
  def *(right: Double) = PMultiply(this, () => right)
  def *(right: () => Double) = PMultiply(this, right)
  def bayes(): () => Double
}

case class P[A](c: Case[A]) extends Probability {
  def apply(): Double = c.probability()
  def bayes() = c.bayes()
}

//case class P2[A, B](c: Case[(A, B)]) extends Probability {
//  def apply(): Double = c.probability()
//  def bayes() = c.bayes()
//}

//case class P3[A, B, C](c: Case[(A, B, C)]) extends Probability {
//  def apply(): Double = c.probability()
//  def bayes() = c.bayes()
//}
