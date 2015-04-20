package axle.algebra.laws

import scala.reflect.ClassTag

import org.scalacheck.Arbitrary
import org.scalacheck.Prop
import org.scalacheck.Prop.forAll
import org.scalacheck.Prop.propBoolean

import org.typelevel.discipline.Laws

import spire.algebra.AdditiveMonoid
import spire.algebra.Eq
import spire.algebra.MetricSpace
import spire.algebra.Order

import spire.implicits.eqOps
import spire.implicits.metricSpaceOps
import spire.implicits.partialOrderOps
import spire.implicits.additiveSemigroupOps

object MetricSpaceLaws {

  def apply[A, B] = new MetricSpaceLaws[A, B] {}
}

trait MetricSpaceLaws[A, B] extends Laws {

  trait MetricSpaceRuleSet extends RuleSet {

    def name: String = "metric space"

    def bases: Seq[(String, Laws#RuleSet)] = Seq()

    def parents: Seq[RuleSet] = Seq.empty

  }

  def cauchySchwarz(
    implicit ms: MetricSpace[A, B],
    cta: ClassTag[A],
    arbA: Arbitrary[A],
    ctb: ClassTag[B],
    ord: Order[B],
    addB: AdditiveMonoid[B]) =
    new MetricSpaceRuleSet {

      def props: Seq[(String, Prop)] = Seq(
        "Cauchy-Schwarz (aka Triangle Inequality)" → forAll { (x: A, y: A, z: A) =>
          (x distance z) <= (x distance y) + (y distance z)
        })
    }

}
