package axle

import org.specs2.mutable._
import spire.math._
import axle.quanta._
import Information._
import axle.stats._

class InformationTheorySpec extends Specification {

  "hard-coded distributions" should {

    "work" in {

      val d = new RandomVariable0("d", Some(List("A", "B", "C").toIndexedSeq),
        distribution = Some(new ConditionalProbabilityTable0(Map("A" -> Real(0.2), "B" -> Real(0.1), "C" -> Real(0.7)))))

      entropy(d).magnitude must be equalTo (1.1567796494470395)
    }
  }

  "cpt" should {
    "work" in {

      val X = RandomVariable0("X", distribution = Some(new ConditionalProbabilityTable0(Map("foo" -> Real(0.1), "food" -> Real(0.9)))))

      val Y = RandomVariable0("Y", distribution = Some(new ConditionalProbabilityTable0(Map("bar" -> Real(0.9), "bard" -> Real(0.1)))))

      val cpt = new ConditionalProbabilityTable2[String, String, String](Map(
        ("foo", "bar") -> Map("a" -> Real(0.3), "b" -> Real(0.7)),
        ("foo", "bard") -> Map("a" -> Real(0.2), "b" -> Real(0.8)),
        ("food", "bar") -> Map("a" -> Real(0.9), "b" -> Real(0.1)),
        ("food", "bard") -> Map("a" -> Real(0.5), "b" -> Real(0.5))
      ))

      val A = RandomVariable2("A", grv1 = X, grv2 = Y,
        distribution = Some(cpt))

      val p = P((A is "a") | (X is "foo") ∧ (Y isnt "bar"))
      val b = P((A is "a") ∧ (X is "foo")).bayes

      // println("p = " + p)
      // println("p() = " + p())
      // println("b = " + b)
      // println("b() = " + b())

      // TODO
      1 should be equalTo (1)
    }
  }

  "coins" should {
    "work" in {

      val biasedCoin = coin(Real(0.9))
      val fairCoin = coin()

      // TODO: figure out why equalTo isn't working here
      entropy(biasedCoin).magnitude should be equalTo (0.46899559358928117)
      entropy(fairCoin).magnitude should be equalTo (1.0)
    }
  }

}
