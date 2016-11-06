package axle.stats

import cats.Show
import spire.algebra.Field
import spire.algebra.Order
import spire.random.Dist

object UnknownDistribution0 {

  implicit def show[A, N]: Show[UnknownDistribution0[A, N]] =
    new Show[UnknownDistribution0[A, N]] {
      def show(t: UnknownDistribution0[A, N]): String = "unknown"
    }

}

case class UnknownDistribution0[A, N: Field: Order: Dist](values: IndexedSeq[A], name: String)
  extends Distribution0[A, N] {

  def probabilityOf(a: A): N = ???

  def map[B](f: A => B): Distribution0[B, N] = ???

  def flatMap[B](f: A => Distribution0[B, N]): Distribution0[B, N] = ???

  def is(v: A): CaseIs[A, N] = CaseIs(this, v)

  def isnt(v: A): CaseIsnt[A, N] = CaseIsnt(this, v)

  def observe(): A = ???

}
