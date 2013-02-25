package axle.algebra

import spire.algebra.{ Semigroup, Monoid }

trait MA[M[_], A] {

  val value: M[A]

  def |+|(a2: M[A])(implicit s: Semigroup[M[A]]) =
    s.op(value, a2)

  def summ(implicit m: Monoid[A], fl: FoldLeft[M]): A =
    fl.foldLeft(value, m.id, m.op)

  def fmap[B](f: A => B)(implicit functor: Functor[M]): M[B] =
    functor.fmap(value, f)

  def bind[B](f: A => M[B])(implicit monad: Monad[M]): M[B] =
    monad.bind(value, f)

}
