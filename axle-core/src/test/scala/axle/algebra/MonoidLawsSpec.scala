package axle.algebra

import spire.algebra._
import spire.implicits._

import org.specs2.ScalaCheck
import org.specs2.mutable._
import org.scalacheck._
import Arbitrary._
import Gen._
import Prop._

class MonoidLaws[A: Eq](monoids: Seq[Monoid[A]]) {

  val leftZero = (m: Monoid[A], x: A) => m.op(m.id, x) === x

  val rightZero = (m: Monoid[A], x: A) => m.op(x, m.id) === x

  val associativity = (m: Monoid[A], x: A, y: A, z: A) => m.op(m.op(x, y), z) === m.op(x, m.op(y, z))

  lazy val genMonoid = oneOf(monoids)

  implicit lazy val arbMonoid = Arbitrary(genMonoid)

}

class IntMonoidLawsSpec extends Specification with ScalaCheck {

  val intMonoids = List(
    implicitly[AdditiveMonoid[Int]].additive,
    implicitly[MultiplicativeMonoid[Int]].multiplicative)

  val laws = new MonoidLaws(intMonoids)

  import laws._

  "Int monoids obey left zero" ! prop { leftZero }
  "Int monoids obey right zero" ! prop { rightZero }
  "Int monoids obey associativity" ! prop { associativity }

}