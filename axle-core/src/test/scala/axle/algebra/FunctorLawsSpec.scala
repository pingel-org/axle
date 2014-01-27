package axle.algebra

import spire.algebra._
import spire.implicits._
import org.specs2.ScalaCheck
import org.specs2.mutable._
import org.scalacheck._
import Arbitrary._
import Gen._
import Prop._

abstract class FunctorLawsSpec[F[_]: Functor, A: Eq: Arbitrary, B: Eq: Arbitrary, C: Eq: Arbitrary](name: String)(implicit eqFa: Eq[F[A]], arbFa: Arbitrary[F[A]], eqFc: Eq[F[C]])
  extends Specification
  with ScalaCheck {

  s"$name obey identity" ! prop { (xs: F[A]) =>
    val functor = implicitly[Functor[F]]
    val lhs: F[A] = functor.fmap[A, A](xs, identity)
    val rhs: F[A] = identity(xs)
    lhs === rhs
  }

  s"$name obey composition" ! prop { (xs: F[A], f: A => B, g: B => C) =>
    val functor = implicitly[Functor[F]]
    val lhs: F[C] = functor.fmap[B, C](functor.fmap[A, B](xs, f), g)
    val rhs: F[C] = functor.fmap[A, C](xs, g compose f)
    lhs === rhs
  }

}

object ArbitraryStuff {

  // TODO
  implicit val arbListInt: Arbitrary[List[Int]] = Arbitrary(Gen.oneOf(List(List(1, 2, 3), List(4, 5, 6))))

}

import ArbitraryStuff._

class ListIntFunctorLawsSpec extends FunctorLawsSpec[List, Int, Int, Int]("List[Int] Functor")
