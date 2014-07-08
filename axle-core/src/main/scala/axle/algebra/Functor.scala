package axle.algebra

trait Functor[F[_]] {

  def fmap[A, B](xs: F[A], f: A => B): F[B]

}

object Functor {

  implicit def ListFunctor: Functor[List] = new Functor[List] {
    def fmap[A, B](list: List[A], f: A => B) = list map f
  }

  implicit def OptFunctor: Functor[Option] = new Functor[Option] {
    def fmap[A, B](opt: Option[A], f: A => B) = opt map f
  }

  implicit def Function1Functor[A]: Functor[({ type λ[α] = (A) => α })#λ] = new Functor[({ type λ[α] = (A) => α })#λ] {
    def fmap[B, C](fn: A => B, f: B => C) = f compose fn
  }

}
