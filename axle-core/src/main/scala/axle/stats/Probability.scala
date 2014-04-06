package axle.stats

import axle.stats._
import spire.math._
import spire.implicits._
import spire.algebra._

trait Probability[N] extends (() => N) {

  def *(right: () => N)(implicit f: Field[N]): () => N = PMultiply(this, right)
  
  def bayes: () => N
}

case class P[A, N: Field](c: Case[A, N]) extends Probability[N] {

  def apply(): N = c.probability()
  
  def bayes: () => N = c.bayes
}

//case class P2[A, B](c: Case[(A, B)]) extends Probability {
//  def apply(): Real = c.probability()
//  def bayes() = c.bayes()
//}

//case class P3[A, B, C](c: Case[(A, B, C)]) extends Probability {
//  def apply(): Real = c.probability()
//  def bayes() = c.bayes()
//}
