package axle.algebra

import scala.Left
import scala.Right

// Writer
// State
// IO
// ParserCombinator
// Future
// Behavior
// Continuation
// Giry

trait Monad[M[_]] {

  def bind[A, B](xs: M[A], f: A => M[B]): M[B]

  // unit/apply/point
  def unit[A](a: A): M[A]
}

object Monad {

//  implicit def monadEq[M[_]]() = new Eq[Monad[M[_]]] {
//    def eqv(x: Monad[A], y: Monad[A]): Boolean = ???
//  }

  implicit val listMonad = new Monad[List] {

    def bind[A, B](list: List[A], f: A => List[B]): List[B] = list.flatMap(f)

    def unit[A](a: A): List[A] = List(a)
  }

  implicit val optionMonad = new Monad[Option] {

    def bind[A, B](opt: Option[A], f: A => Option[B]): Option[B] = opt.flatMap(f)

    def unit[A](a: A): Option[A] = Some(a)
  }

  implicit def eitherMonad[A] = new Monad[({ type λ[α] = Either[A, α] })#λ] {

    def bind[B, C](either: Either[A, B], f: B => Either[A, C]): Either[A, C] = either match {
      case Left(a) => Left(a)
      case Right(b) => f(b)
    }

    def unit[B](b: B): Either[A, B] = Right(b)
  }

}
