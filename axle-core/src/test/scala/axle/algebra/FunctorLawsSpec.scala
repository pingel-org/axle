package axle.algebra

import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary._
import org.scalatest._
import org.typelevel.discipline.scalatest.Discipline

import axle.algebra.laws.FunctorLaws
import cats.implicits._
import cats.kernel.Eq

class FunctorLawsSpec
    extends FunSuite with Matchers
    with Discipline {

  implicit def eqF1AB[A: Arbitrary, B: Eq]: Eq[A => B] =
    new Eq[A => B] {
      val arbA = implicitly[Arbitrary[A]]
      val eqB = Eq[B]
      // TODO: Is this available in ScalaCheck?
      def eqv(f: A => B, g: A => B): Boolean = {
        (1 to 10) forall { i =>
          val a = arbA.arbitrary.sample.get // TODO when does sample return None?
          eqB.eqv(f(a), g(a))
        }
      }
    }

  checkAll("List[Int]", FunctorLaws[List[Int], Int].functorIdentity)
  checkAll("List[String]", FunctorLaws[List[String], String].functorIdentity)
  checkAll("Option[Int]", FunctorLaws[Option[Int], Int].functorIdentity)
  checkAll("Function1[Int, Int]", FunctorLaws[Int => Int, Int].functorIdentity)
  //({ type λ[α] = Int => α })#λ

  checkAll("List[Int]", FunctorLaws[List[Int], Int].functorComposition[Int, Int, List[Int], List[Int]])
  checkAll("List[String]", FunctorLaws[List[String], String].functorComposition[String, String, List[String], List[String]])
  checkAll("Option[Int]", FunctorLaws[Option[Int], Int].functorComposition[Int, Int, Option[Int], Option[Int]])
  checkAll("Function1[Int, Int]", FunctorLaws[Int => Int, Int].functorComposition[Int, Int, Int => Int, Int => Int])

}
