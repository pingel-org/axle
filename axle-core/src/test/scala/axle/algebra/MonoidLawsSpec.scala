package axle.algebra

import org.scalacheck.Arbitrary.arbInt
import org.scalatest._
import org.typelevel.discipline.scalatest.Discipline
import axle.algebra.laws.MonoidLaws
import spire.algebra.AdditiveMonoid
import spire.algebra.MultiplicativeMonoid
import spire.implicits.IntAlgebra
import cats.implicits._

class MonoidLawsSpec() extends FunSuite with Matchers with Discipline {

  //  lazy val genMonoid: Gen[Monoid[A]] = Gen.oneOf(monoids)
  //
  //  implicit lazy val arbMonoid: Arbitrary[Monoid[A]] = Arbitrary(genMonoid)

  checkAll("implicitly[AdditiveMonoid[Int]].additive", MonoidLaws[Int].monoidLaws(implicitly[AdditiveMonoid[Int]].additive))

  checkAll("implicitly[MultiplicativeMonoid[Int]].multiplicative", MonoidLaws[Int].monoidLaws(implicitly[MultiplicativeMonoid[Int]].multiplicative))

}
