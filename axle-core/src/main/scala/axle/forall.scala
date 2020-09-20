package axle

import cats.Functor
import cats.implicits._
import spire.algebra.Bool
import axle.algebra.Aggregatable
import axle.syntax.aggregatable._

object ∀ extends {

  def apply = forall.apply _
}

object forall {

  def apply[A, B, F[_]](
    as: F[A])(
    predicate: A => B)(
    implicit
    bool:    Bool[B],
    functor: Functor[F],
    agg:     Aggregatable[F]): B =
    as.map(predicate).aggregate(bool.one)(bool.and, bool.and) // TODO short-circuit

}
