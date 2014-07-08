package axle

import scala.collection.GenTraversable

import spire.algebra.AdditiveMonoid

object Σ {

  def apply[T, N: AdditiveMonoid](gt: GenTraversable[T])(f: T => N): N = {
    val monoid = implicitly[AdditiveMonoid[N]]
    gt.aggregate(monoid.zero)({ case (x, y) => monoid.plus(x, f(y)) }, monoid.plus)
  }
}
