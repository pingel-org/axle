package axle.algebra

trait Functor[F[_]] {

  def mapp[A, B](xs: F[A], f: A => B): F[B]

}

object Functor {

  def id[T](x: T) = x

  def checkAxiom1[F[_]: Functor, A](xs: F[A]): Boolean =
    xs.mapp(id) === id(xs)

  def checkAxiom2[F[_]: Functor, A, B, C](xs: F[A], f: A => B, g: B => C): Boolean =
    xs.mapp(f).mapp(g) === xs.mapp(g compose f)

  implicit val listFunctor = new Functor[List] {
    def mapp[A, B](xs: List[A], f: A => B): List[B] = xs.map(f)
  }

  implicit val optFunctor = new Functor[Option] {
    def mapp[A, B](xs: Option[A], f: A => B): Option[B] = xs.map(f)
  }

  // TODO: Function as functor

}