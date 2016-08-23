package axle.algebra.laws

import org.scalacheck.Arbitrary
import org.scalacheck.Prop
import org.scalacheck.Prop.forAll
import org.scalacheck.Prop.propBoolean
import org.typelevel.discipline.Laws

import axle.algebra.Applicative
import spire.algebra.Eq
import spire.implicits.eqOps

object ApplicativeLaws {

  def apply[F[_], A] =
    new ApplicativeLaws[F, A] {}
}

trait ApplicativeLaws[F[_], A] extends Laws {

  trait ApplicativeRuleSet extends RuleSet {

    def name: String = "applicative"

    def bases: Seq[(String, Laws#RuleSet)] = Seq()

    def parents: Seq[RuleSet] = Seq.empty

  }

  def leftIdentity(implicit app: Applicative[F], arbFa: Arbitrary[F[A]], eqFa: Eq[F[A]]) =
    new ApplicativeRuleSet {

      def props: Seq[(String, Prop)] = Seq(
        // pure id <*> v = v
        "left identity" → forAll { (v: F[A]) =>
          val lhs: F[A] = app.<*>(app.pure(identity[A] _)).apply(v)
          lhs === v
        })
    }

  // pure (.) <*> u <*> v <*> w = u <*> (v <*> w)
  //  s"$name obey axiom 2" ! prop { (u: A, v: A, w: A) =>
  //    val applicative = Applicative[F]
  //    // TODO
  //    val lhs = 4
  //    val rhs = 4
  //    lhs === rhs
  //  }

  // pure f <*> pure x = pure (f x)
  def axiom3[B](implicit app: Applicative[F], arbA: Arbitrary[A], arbFa: Arbitrary[F[A]], eqFa: Eq[F[A]], eqFb: Eq[F[B]], arbAB: Arbitrary[A => B]) =
    new ApplicativeRuleSet {

      def props: Seq[(String, Prop)] = Seq(
        "axiom 3" → forAll { (x: A, f: A => B) =>
          val lhs: F[B] = ((app <*> app.pure(f)) compose app.pure[F, A]).apply(x)
          val rhs: F[B] = app.pure(f(x))
          lhs === rhs
        })
    }

  // u <*> pure y = pure ($ y) <*> u
  //  s"$name obey axiom 4" ! prop { (u: A => B, y: A => B) =>
  //    val applicative = Applicative[F]
  //    val lhs = (applicative pure (applicative <*> u))(y)
  //    val rhs = (applicative <*> (applicative pure y))(u)
  //    lhs === rhs
  //  }

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
