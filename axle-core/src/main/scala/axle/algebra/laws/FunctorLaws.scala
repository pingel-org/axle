package axle.algebra.laws

import org.scalacheck.Arbitrary
import org.scalacheck.Prop
import org.scalacheck.Prop.forAll
import org.scalacheck.Prop.propBoolean
import org.typelevel.discipline.Laws

import axle.algebra.Functor
import cats.kernel.Eq
import cats.implicits._

object FunctorLaws {

  def apply[F, A] = new FunctorLaws[F, A] {}
}

trait FunctorLaws[F, A] extends Laws {

  trait FunctorRuleSet extends RuleSet {

    def name: String = "functor"

    def bases: Seq[(String, Laws#RuleSet)] = Seq()

    def parents: Seq[RuleSet] = Seq.empty

  }

  def functorIdentity[G](
    implicit
    functor: Functor[F, A, A, F],
    arbF:    Arbitrary[F],
    eqF:     Eq[F]) =
    new FunctorRuleSet {

      def props: Seq[(String, Prop)] = Seq(
        "identity" → forAll { (xs: F) =>
          val lhs: F = functor.map(xs)(identity)
          val rhs: F = identity(xs)
          lhs === rhs
        })
    }

  def functorComposition[B, C, G, H](
    implicit
    functorFabG: Functor[F, A, B, G],
    functorGbcH: Functor[G, B, C, H],
    functorFacH: Functor[F, A, C, H],
    arbFa:       Arbitrary[F],
    arbAB:       Arbitrary[A => B],
    arbBC:       Arbitrary[B => C],
    eqH:         Eq[H]) =
    new FunctorRuleSet {

      def props: Seq[(String, Prop)] = Seq(
        "composition" → forAll { (xs: F, f: A => B, g: B => C) =>
          val lhs: H = functorGbcH.map(functorFabG.map(xs)(f))(g)
          val rhs: H = functorFacH.map(xs)(g compose f)
          lhs === rhs
        })
    }

}
