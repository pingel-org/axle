package axle.algebra

trait Applicative[F[_]] {

  def pure[F[_]: Applicative, T](x: T): F[T]

  def <*>[F[_]: Applicative, A, B](f: F[A => B]): F[A] => F[B]

}

object Applicative {

  def id[T](x: T) = x
  
  // pure id <*> v = v

  def checkAxiom1[F[_] : Applicative, T](v: T): Boolean = {
    //val applicative = implicitly[Applicative[F]]
    //applicative.pure(id <*> v) == v
    true
  }

  // pure (.) <*> u <*> v <*> w = u <*> (v <*> w)

  def checkAxiom2[F[_]: Applicative, T](u: T, v: T, w: T): Boolean =
    true

  // pure f <*> pure x = pure (f x)

  def checkAxiom3[F[_]: Applicative, T, U](x: T, f: T => U): Boolean =
    true

  // u <*> pure y = pure ($ y) <*> u  

  def checkAxiom4[F[_]: Applicative, T](v: T): Boolean =
    true

}
