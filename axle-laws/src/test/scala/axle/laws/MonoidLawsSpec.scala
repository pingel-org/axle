package axle.laws

import org.scalacheck.Arbitrary.arbInt
import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers
import org.typelevel.discipline.scalatest.Discipline

import cats.implicits._

import spire.algebra._

class MonoidLawsSpec() extends AnyFunSuite with Matchers with Discipline {

  implicit val intRing: Ring[Int] = spire.implicits.IntAlgebra

  //  lazy val genMonoid: Gen[Monoid[A]] = Gen.oneOf(monoids)
  //
  //  implicit lazy val arbMonoid: Arbitrary[Monoid[A]] = Arbitrary(genMonoid)

  checkAll("implicitly[AdditiveMonoid[Int]].additive", MonoidLaws[Int].monoidLaws(implicitly[AdditiveMonoid[Int]].additive))

  checkAll("implicitly[MultiplicativeMonoid[Int]].multiplicative", MonoidLaws[Int].monoidLaws(implicitly[MultiplicativeMonoid[Int]].multiplicative))

}
