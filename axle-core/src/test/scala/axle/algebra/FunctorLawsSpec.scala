package axle.algebra

import org.scalacheck.Arbitrary
import org.specs2.mutable.Specification
import org.typelevel.discipline.specs2.mutable.Discipline

import spire.algebra.Eq
import spire.implicits.eqOps

class FunctorLawsSpec
  extends Specification
  with Discipline {

  implicit def eqF1AB[A: Arbitrary, B: Eq]: Eq[A => B] =
    new Eq[A => B] {
      val arbA = implicitly[Arbitrary[A]]
      // TODO: Is this available in ScalaCheck?
      def eqv(f: A => B, g: A => B): Boolean = {
        (1 to 10) forall { i =>
          val a = arbA.arbitrary.sample.get // TODO when does sample return None?
          f(a) === g(a)
        }
      }
    }

  checkAll("List[Int]", FunctorLaws[List, Int].functorIdentity)
  checkAll("List[String]", FunctorLaws[List, String].functorIdentity)
  checkAll("Option[Int]", FunctorLaws[Option, Int].functorIdentity)
  checkAll("List[String]", FunctorLaws[List, String].functorIdentity)
  checkAll("Function1[Int, Int]", FunctorLaws[({ type λ[α] = Int => α })#λ, Int].functorIdentity)

  checkAll("List[Int]", FunctorLaws[List, Int].functorComposition[Int, Int])
  checkAll("List[String]", FunctorLaws[List, String].functorComposition[String, String])
  checkAll("Option[Int]", FunctorLaws[Option, Int].functorComposition[Int, Int])
  checkAll("List[String]", FunctorLaws[List, String].functorComposition[String, String])
  checkAll("Function1[Int, Int]", FunctorLaws[({ type λ[α] = Int => α })#λ, Int].functorComposition[Int, Int])

}
