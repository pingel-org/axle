package axle.stats

import spire.algebra.Field

trait Probability[N] extends Function0[N] {

  def *(right: () => N)(implicit f: Field[N]): () => N = PMultiply(this, right)

  def bayes: () => N
}

case class P[A, N: Field](c: Case[A, N]) extends Probability[N] {

  def apply(): N = c.probability()

  def bayes: () => N = c.bayes
}

