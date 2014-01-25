package axle.algebra

import spire.algebra._
import spire.implicits._
import org.specs2.ScalaCheck
import org.specs2.mutable._
import org.scalacheck._
import Arbitrary._
import Gen._
import Prop._

abstract class MonoidLawsSpec[A: Eq: Arbitrary](name: String, monoids: Seq[Monoid[A]])
  extends Specification with ScalaCheck {

  lazy val genMonoid: Gen[Monoid[A]] = Gen.oneOf(monoids)

  implicit lazy val arbMonoid: Arbitrary[Monoid[A]] = Arbitrary(genMonoid)

  s"$name obey left zero" ! prop { (m: Monoid[A], x: A) =>
    m.op(m.id, x) === x
  }

  s"$name obey right zero" ! prop { (m: Monoid[A], x: A) =>
    m.op(x, m.id) === x
  }

  s"$name obey associativity" ! prop { (m: Monoid[A], x: A, y: A, z: A) =>
    m.op(m.op(x, y), z) === m.op(x, m.op(y, z))
  }

}

class IntMonoidLawsSpec extends MonoidLawsSpec("Int monoids", List(
  implicitly[AdditiveMonoid[Int]].additive,
  implicitly[MultiplicativeMonoid[Int]].multiplicative))
