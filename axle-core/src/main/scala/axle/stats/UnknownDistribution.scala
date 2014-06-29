package axle.stats

import spire.algebra.Field
import spire.algebra.Order
import spire.random.Dist

class UnknownDistribution0[A, N: Field: Order: Dist](_values: IndexedSeq[A])
extends Distribution0[A, N] {

  def values: IndexedSeq[A] = _values
  
  def probabilityOf(a: A): N = ???

  def map[B](f: A => B): Distribution0[B, N] = ???

  def flatMap[B](f: A => Distribution0[B, N]): Distribution0[B, N] = ???
  
  def observe(): A = ???
}
