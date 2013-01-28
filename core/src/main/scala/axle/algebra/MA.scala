package axle.algebra

trait MA[M[_], A] {

  val value: M[A]
  
  def |+|(a2: M[A])(implicit m: Monoid[M[A]]) = m.mappend(value, a2)

  def summ(implicit m: Monoid[A], fl: FoldLeft[M]): A = fl.foldLeft(value, m.mzero, m.mappend)

  def fmap[B](f: A => B)(implicit functor: Functor[M]): M[B] = functor.fmap(value, f)

  def bind[B](f: A => M[B])(implicit monad: Monad[M]): M[B] =
    monad.bind(value, f)

}
