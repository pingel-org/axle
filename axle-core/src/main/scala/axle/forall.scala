package axle

import axle.algebra.Aggregatable
import axle.algebra.Functor
import axle.syntax.functor._
import axle.syntax.aggregatable._
import spire.algebra.Bool

object forall {

  def apply[A, B, F, G](
    as: F)(
    predicate: A => B)(
    implicit
    bool:    Bool[B],
    functor: Functor[F, A, B, G],
    agg:     Aggregatable[G, B, B]): B =
    as.map(predicate).aggregate(bool.one)(bool.and, bool.and) // TODO short-circuit

}
