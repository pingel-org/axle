package axle.algebra

import scala.annotation.implicitNotFound

@implicitNotFound("Witness not found for Applicative[${F}]")
trait Applicative[F[_]] {

  def pure[F[_]: Applicative, T](x: T): F[T]

  def <*>[F[_]: Applicative, A, B](f: F[A => B]): F[A] => F[B]

}

object Applicative {

  def apply[F[_]: Applicative]: Applicative[F] = implicitly[Applicative[F]]

}