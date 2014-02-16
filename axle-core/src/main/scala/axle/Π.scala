package axle

import spire.algebra.MultiplicativeMonoid
import scala.collection.GenTraversable

object Π {

  def apply[T, N: MultiplicativeMonoid](gt: GenTraversable[T])(f: T => N): N = {
    val monoid = implicitly[MultiplicativeMonoid[N]]
    gt.aggregate(monoid.one)({ case (x, y) => monoid.times(x, f(y)) }, monoid.times(_, _))
  }

}
