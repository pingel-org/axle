package axle.stats

import spire.algebra.Field
import spire.algebra.Order
import spire.random.Dist

class UnknownDistribution0[A, N: Field: Order: Dist] extends Distribution0[A, N] {

  def probabilityOf(a: A): N = ???

  def observe(): A = ???
}
