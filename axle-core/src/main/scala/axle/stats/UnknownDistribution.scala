package axle.stats

import spire.algebra.Field
import spire.algebra.Order
import spire.random.Dist

class UnknownDistribution0[A, N: Field: Order: Dist](_values: IndexedSeq[A])
extends Distribution0[A, N] {

  def values: IndexedSeq[A] = _values
  
  def probabilityOf(a: A): N = ???

  def observe(): A = ???
}
