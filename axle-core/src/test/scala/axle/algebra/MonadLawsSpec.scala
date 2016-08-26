package axle.algebra

import axle.algebra.laws._
import spire.algebra._
import spire.implicits._

import org.specs2.mutable._
import org.scalacheck._

import Arbitrary._
import Gen._

import org.typelevel.discipline.specs2.mutable.Discipline

abstract class MonadLawsSpec
  extends Specification
  with Discipline {

  implicit def eqEitherIntInt[L: Eq, R: Eq]: Eq[Either[L, R]] = new Eq[Either[L, R]] {
    def eqv(x: Either[L, R], y: Either[L, R]): Boolean = (x, y) match {
      case (Left(xl), Left(yl)) if xl === yl   => true
      case (Right(xr), Right(yr)) if xr === yr => true
      case _                                   => false
    }
  }

  checkAll("List[Int]", MonadLaws[List, Int].leftIdentity[Int])
  checkAll("List[String]", MonadLaws[List, String].leftIdentity[String])
  checkAll("Option[Int]", MonadLaws[Option, Int].leftIdentity[Int])
  checkAll("Either[({ type λ[α] = Either[Int, α] })#λ, Int]", MonadLaws[({ type λ[α] = Either[Int, α] })#λ, Int].leftIdentity[Int])

  checkAll("List[Int]", MonadLaws[List, Int].rightIdentity)
  checkAll("List[String]", MonadLaws[List, String].rightIdentity)
  checkAll("Option[Int]", MonadLaws[Option, Int].rightIdentity)
  checkAll("Either[({ type λ[α] = Either[Int, α] })#λ, Int]", MonadLaws[({ type λ[α] = Either[Int, α] })#λ, Int].rightIdentity)

  checkAll("List[Int]", MonadLaws[List, Int].associativity[Int, Int])
  checkAll("List[String]", MonadLaws[List, String].associativity[String, String])
  checkAll("Option[Int]", MonadLaws[Option, Int].associativity[Int, Int])
  checkAll("Either[({ type λ[α] = Either[Int, α] })#λ, Int]", MonadLaws[({ type λ[α] = Either[Int, α] })#λ, Int].associativity[Int, Int])

}
