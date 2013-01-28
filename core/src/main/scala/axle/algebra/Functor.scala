package axle.algebra

trait Functor[F[_]] {

  def fmap[A, B](xs: F[A], f: A => B): F[B]

}

object Functor {

  def id[T](x: T) = x

  def checkIdentity[F[_]: Functor, A](xs: F[A]): Boolean =
    xs.fmap(id) === id(xs)

  def checkComposition[F[_]: Functor, A, B, C](xs: F[A], f: A => B, g: B => C): Boolean =
    xs.fmap(f).fmap(g) === xs.fmap(g compose f)

  implicit val listFunctor = new Functor[List] {
    def fmap[A, B](xs: List[A], f: A => B): List[B] = xs.map(f)
  }

  implicit val optFunctor = new Functor[Option] {
    def fmap[A, B](xs: Option[A], f: A => B): Option[B] = xs.map(f)
  }

  // TODO: Function as functor

}
