package axle

import scala.collection.GenTraversable

import spire.algebra.MultiplicativeMonoid

object Π {

  def apply[T, N: MultiplicativeMonoid](gt: GenTraversable[T])(f: T => N): N = {
    val monoid = implicitly[MultiplicativeMonoid[N]]
    gt.aggregate(monoid.one)({ case (x, y) => monoid.times(x, f(y)) }, monoid.times)
  }

}
