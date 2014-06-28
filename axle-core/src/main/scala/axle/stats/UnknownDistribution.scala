package axle.stats

import spire.algebra.Field
import spire.algebra.Order
import spire.random.Dist

class UnknownDistribution0[A, N: Field: Order: Dist](_values: IndexedSeq[A])
extends Distribution0[A, N] {

  def values: IndexedSeq[A] = _values
  
  def probabilityOf(a: A): N = ???

  def map[B](f: A => B): Distribution[B, N] = ???

  def flatMap[B](f: A => Distribution[B, N]): Distribution[B, N] = ???
  
  def observe(): A = ???
}
