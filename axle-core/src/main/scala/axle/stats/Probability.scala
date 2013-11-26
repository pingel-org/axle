package axle.stats

import axle.stats._
import spire.math._
import spire.implicits._

trait Probability extends (() => Real) {
  def *(right: => Real) = PMultiply(this, right)
  def bayes(): () => Real
}

case class P[A](c: Case[A]) extends Probability {
  def apply(): Real = c.probability()
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
