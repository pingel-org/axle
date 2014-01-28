package axle.algebra

trait Applicative[F[_]] {

  def pure[F[_]: Applicative, T](x: T): F[T]

  def <*>[F[_]: Applicative, A, B](f: F[A => B]): F[A] => F[B]

}
