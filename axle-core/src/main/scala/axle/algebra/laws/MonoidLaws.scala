package axle.algebra.laws

import org.scalacheck.Arbitrary
import org.scalacheck.Prop
import org.scalacheck.Prop.forAll
import org.scalacheck.Prop.propBoolean
import org.typelevel.discipline.Laws

import spire.algebra.Monoid
import spire.algebra.Eq
import spire.implicits.eqOps

object MonoidLaws {

  def apply[A] = new MonoidLaws[A] {}
}

trait MonoidLaws[A] extends Laws {

  trait MonoidRuleSet extends RuleSet {

    def name: String = "monoid"

    def bases: Seq[(String, Laws#RuleSet)] = Seq()

    def parents: Seq[RuleSet] = Seq.empty

  }

  def monoidLaws(m: Monoid[A])(implicit eqA: Eq[A], arbA: Arbitrary[A]) =
    new MonoidRuleSet {

      def props: Seq[(String, Prop)] = Seq(
        "left zero" → forAll { (x: A) =>
          m.op(m.id, x) === x
        },
        "right zero" → forAll { (x: A) =>
          m.op(x, m.id) === x
        },
        "associativity" → forAll { (x: A, y: A, z: A) =>
          m.op(m.op(x, y), z) === m.op(x, m.op(y, z))
        })
    }

}
