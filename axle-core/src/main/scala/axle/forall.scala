package axle

import spire.algebra.BooleanAlgebra

object forall {

  def apply[T, A](as: Iterable[T])(predicate: T => A)(implicit ev: BooleanAlgebra[A]): A =
    as.map(predicate).aggregate(ev.one)(ev.and, ev.and) // TODO short-circuit

}