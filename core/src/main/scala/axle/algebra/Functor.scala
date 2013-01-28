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
    def fmap[A, B](list: List[A], f: A => B): List[B] = list.map(f)
  }

  implicit val optFunctor = new Functor[Option] {
    def fmap[A, B](opt: Option[A], f: A => B): Option[B] = opt.map(f)
  }

  implicit def function1Functor[A] = new Functor[({ type λ[α] = Function1[A, α] })#λ] {
    def fmap[B, C](fn: Function1[A, B], f: B => C): Function1[A, C] = f.compose(fn)
  }

}
