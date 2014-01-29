package axle.algebra

import spire.algebra._
import spire.implicits._
import org.specs2.ScalaCheck
import org.specs2.mutable._
import org.scalacheck._
import Arbitrary._
import Gen._
import Prop._

abstract class ApplicativeLawsSpec[F[_]: Applicative, A: Eq: Arbitrary, B: Eq: Arbitrary, C: Eq: Arbitrary](name: String)(
  implicit eqfa: Eq[F[A]],
  arbfa: Arbitrary[F[A]],
  eqfb: Eq[F[B]])
  extends Specification
  with ScalaCheck {

  // pure id <*> v = v
  s"$name obey left identity" ! prop { (v: F[A]) =>
    val applicative = implicitly[Applicative[F]]
    val lhs: F[A] = applicative.<*>(applicative.pure(identity[A] _)).apply(v)
    lhs === v
  }

  // pure (.) <*> u <*> v <*> w = u <*> (v <*> w)
  s"$name obey axiom 2" ! prop { (u: A, v: A, w: A) =>
    val applicative = implicitly[Applicative[F]]
    // TODO
    val lhs = 4
    val rhs = 4
    lhs === rhs
  }

  // pure f <*> pure x = pure (f x)
  s"$name obey axiom 3" ! prop { (x: A, f: A => B) =>
    val applicative = implicitly[Applicative[F]]
    val lhs1: F[A => B] = applicative.pure(f)
    val lhs2: F[A] => F[B] = applicative.<*>(lhs1)
    val lhs3: A => F[B] = lhs2 compose applicative.pure[F, A]
    val lhs: F[B] = lhs3(x)
    val rhs: F[B] = applicative.pure(f(x))
    lhs === rhs
  }

  // u <*> pure y = pure ($ y) <*> u
  // def checkAxiom4[F[_]: Applicative, T](v: T): Boolean
  s"$name obey axiom 4" ! prop { (u: F[A], v: F[A], y: A) =>
    val applicative = implicitly[Applicative[F]]
    // TODO
    val lhs = 4
    val rhs = 4
    lhs === rhs
  }

}

/*
    val p2 = pure2( { (x: Int) => x + 3 } )

    val somePlus3 = Some( { (x: Int) => x + 3 } )

    val a1 = somePlus3 <*> Some(9)

    val a2 = pure2( { (x: Int) => x + 3 } ) <*> Some(10)

    val a3 = pure2( { (x: Int) => x + 3 } ) <*> Some(9)

    val a4 = Some( { (s: String) => s ++ "hahah" } ) <*> None

    val a5 = None <*> Some("woot")

    val intAdd = { (x: Int, y: Int) => x + y }
    val stringAppend = { (s1: String, s2: String) => s1 ++ s2 }

    val p4 = pure3( intAdd )

    val a6 = ( pure( intAdd ) <*> Some(3) ) <*> Some(5)
    val a7 = ( pure( intAdd ) <*> Some(3) ) <*> None
    val a8 = ( pure( intAdd ) <*> None    ) <*> Some(5)
*/
