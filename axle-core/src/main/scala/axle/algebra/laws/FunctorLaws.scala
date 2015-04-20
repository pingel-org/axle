package axle.algebra.laws

import scala.annotation.implicitNotFound
import scala.reflect.ClassTag

import org.scalacheck.Arbitrary
import org.scalacheck.Prop
import org.scalacheck.Prop.forAll
import org.scalacheck.Prop.propBoolean
import org.typelevel.discipline.Laws

import axle.algebra.Functor
import spire.algebra.Eq
import spire.implicits.eqOps

object FunctorLaws {

  def apply[F[_], A] =
    new FunctorLaws[F, A] {}
}

trait FunctorLaws[F[_], A] extends Laws {

  def functorIdentity(implicit functor: Functor[F], arbFa: Arbitrary[F[A]], cta: ClassTag[A], eqFa: Eq[F[A]]) =
    new RuleSet {

      def name: String = "functor"

      def bases: Seq[(String, Laws#RuleSet)] = Seq()

      def parents: Seq[RuleSet] = Seq.empty

      def props: Seq[(String, Prop)] = Seq(
        "identity" → forAll { (xs: F[A]) =>
          val lhs: F[A] = functor.map[A, A](xs)(identity)
          val rhs: F[A] = identity(xs)
          lhs === rhs
        })
    }

  def functorComposition[B: ClassTag, C: ClassTag](
    implicit functor: Functor[F],
    arbFa: Arbitrary[F[A]],
    arbAB: Arbitrary[A => B],
    arbBC: Arbitrary[B => C],
    cta: ClassTag[A],
    eqFa: Eq[F[A]],
    eqFb: Eq[F[B]],
    eqFc: Eq[F[C]]) =
    new RuleSet {

      def name: String = "functor"

      def bases: Seq[(String, Laws#RuleSet)] = Seq.empty

      def parents: Seq[RuleSet] = Seq.empty

      def props: Seq[(String, Prop)] = Seq(
        "composition" → forAll { (xs: F[A], f: A => B, g: B => C) =>
          val lhs: F[C] = functor.map[B, C](functor.map[A, B](xs)(f))(g)
          val rhs: F[C] = functor.map[A, C](xs)(g compose f)
          lhs === rhs
        })
    }

}
