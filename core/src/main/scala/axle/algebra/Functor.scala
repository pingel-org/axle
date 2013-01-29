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

  implicit def ListFunctor = new Functor[List] {
    def fmap[A, B](list: List[A], f: A => B) = list.map(f)
  }

  implicit def OptFunctor = new Functor[Option] {
    def fmap[A, B](opt: Option[A], f: A => B) = opt.map(f)
  }

  implicit def Function1Functor[A] = new Functor[({ type λ[α] = (A) => α })#λ] {
    def fmap[B, C](fn: A => B, f: B => C) = f.compose(fn)
  }

}
