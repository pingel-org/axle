package axle.logic

import cats.Functor
import cats.implicits._
import spire.algebra.Bool
import axle.algebra.Aggregatable
import axle.syntax.aggregatable._

object ∃ {

  def apply[A, B, F[_]](
    as: F[A])(
    predicate: A => B)(
    implicit
    bool:    Bool[B],
    functor: Functor[F],
    agg:     Aggregatable[F]): B = thereexists.apply(as)(predicate)
}

object thereexists {

  def apply[A, B, F[_]](
    as: F[A])(
    predicate: A => B)(
    implicit
    bool:    Bool[B],
    functor: Functor[F],
    agg:     Aggregatable[F]): B =
    as.map(predicate).aggregate(bool.zero)(bool.or, bool.or) //TODO short-circuit

}
