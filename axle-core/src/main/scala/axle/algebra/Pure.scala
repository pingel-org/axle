package axle.algebra

import spire.algebra.Monoid

trait Pure[P[_]] {

  def pure[A](a: => A): P[A]
}

object Pure {

  implicit def IdentityPure: Pure[Identity] = new Pure[Identity] {
    def pure[A](a: => A) = a
  }

  implicit def ListPure: Pure[List] = new Pure[List] {
    def pure[A](a: => A) = List(a)
  }

  implicit def OptionPure: Pure[Option] = new Pure[Option] {
    def pure[A](a: => A) = Some(a)
  }

  implicit def Function1Pure[R]: Pure[({ type λ[α] = (R) => α })#λ] = new Pure[({ type λ[α] = (R) => α })#λ] {
    def pure[A](a: => A) = (_: R) => a
  }

  implicit def EitherPure[L]: Pure[({ type λ[α] = Either[L, α] })#λ] = new Pure[({ type λ[α] = Either[L, α] })#λ] {
    def pure[A](a: => A) = Right(a)
  }

  implicit def Tuple2Pure[R: Monoid]: Pure[({ type λ[α] = (R, α) })#λ] = new Pure[({ type λ[α] = (R, α) })#λ] {
    def pure[A](a: => A) = (∅[R], a)
  }

  implicit def Tuple3Pure[R: Monoid, S: Monoid]: Pure[({ type λ[α] = (R, S, α) })#λ] = new Pure[({ type λ[α] = (R, S, α) })#λ] {
    def pure[A](a: => A) = (∅[R], ∅[S], a)
  }

  implicit def Tuple4Pure[R: Monoid, S: Monoid, T: Monoid]: Pure[({ type λ[α] = (R, S, T, α) })#λ] = new Pure[({ type λ[α] = (R, S, T, α) })#λ] {
    def pure[A](a: => A) = (∅[R], ∅[S], ∅[T], a)
  }

}