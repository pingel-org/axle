package axle.algebra

import scala.annotation.implicitNotFound

@implicitNotFound("No member of typeclass Applicative found for type ${F}")
trait Applicative[F[_]] {

  def pure[F[_]: Applicative, T](x: T): F[T]

  def <*>[F[_]: Applicative, A, B](f: F[A => B]): F[A] => F[B]

}
