package axle.algebra.laws

import org.typelevel.discipline.Laws
import axle.algebra.Monad
import org.scalacheck.Arbitrary
import org.scalacheck.Prop
import org.scalacheck.Prop.forAll
import org.scalacheck.Prop.propBoolean
import org.typelevel.discipline.Laws
import spire.algebra.Eq
import spire.implicits.eqOps

object MonadLaws {

  def apply[M[_], A] = new MonadLaws[M, A] {}
}

trait MonadLaws[M[_], A] extends Laws {

  trait MonadRuleSet extends RuleSet {

    def name: String = "monad"

    def bases: Seq[(String, Laws#RuleSet)] = Seq()

    def parents: Seq[RuleSet] = Seq.empty

  }

  def leftIdentity[B](implicit monad: Monad[M], arbMa: Arbitrary[M[A]], arbA: Arbitrary[A], arbAMb: Arbitrary[A => M[B]], eqMb: Eq[M[B]]) =
    new MonadRuleSet {
      def props: Seq[(String, Prop)] = Seq(
        "left identity" → forAll { (m: M[A], x: A, f: A => M[B]) =>
          val lhs: M[B] = monad.bind(monad.unit(x))(f)
          val rhs: M[B] = f(x)
          lhs === rhs
        })
    }

  def rightIdentity(implicit monad: Monad[M], arbMa: Arbitrary[M[A]], eqMa: Eq[M[A]]) =
    new MonadRuleSet {
      def props: Seq[(String, Prop)] = Seq(
        "right identity" → forAll { (m: M[A]) =>
          val lhs: M[A] = monad.bind(m)(monad.unit)
          val rhs: M[A] = m
          lhs === rhs
        })
    }

  def associativity[B, C](implicit monad: Monad[M], arbMa: Arbitrary[M[A]], arbAMb: Arbitrary[A => M[B]], arbBMc: Arbitrary[B => M[C]], eqMc: Eq[M[C]]) =
    new MonadRuleSet {
      def props: Seq[(String, Prop)] = Seq(
        "associativity" → forAll { (m: M[A], f: A => M[B], g: B => M[C]) =>
          val lhs: M[C] = monad.bind(monad.bind(m)(f))(g)
          val rhs: M[C] = monad.bind(m)((x: A) => monad.bind(f(x))(g))
          lhs === rhs
        })
    }

}