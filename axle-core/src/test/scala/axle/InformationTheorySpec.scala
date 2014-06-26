package axle

import org.specs2.mutable.Specification

import axle.stats.ConditionalProbabilityTable0
import axle.stats.ConditionalProbabilityTable2
import axle.stats.P
import axle.stats.RandomVariable0
import axle.stats.RandomVariable2
import axle.stats.coin
import axle.stats.entropy
import axle.stats.rationalProbabilityDist
import spire.algebra.Order
import spire.math.Number.apply
import spire.math.Rational

class InformationTheorySpec extends Specification {

  implicit val orderStrings = Order.from(implicitly[Ordering[String]].compare)

  "hard-coded distributions" should {

    "work" in {

      val d = new RandomVariable0("d",
        new ConditionalProbabilityTable0(Map(
          "A" -> Rational(2, 10),
          "B" -> Rational(1, 10),
          "C" -> Rational(7, 10))))

      entropy(d).magnitude must be equalTo (1.1567796494470395)
    }
  }

  "cpt" should {
    "work" in {

      val X = RandomVariable0("X", new ConditionalProbabilityTable0(Map(
        "foo" -> Rational(1, 10),
        "food" -> Rational(9, 10))))

      val Y = RandomVariable0("Y", new ConditionalProbabilityTable0(Map(
        "bar" -> Rational(9, 10),
        "bard" -> Rational(1, 10))))

      val cpt = new ConditionalProbabilityTable2(Map(
        ("foo", "bar") -> Map("a" -> Rational(3, 10), "b" -> Rational(7, 10)),
        ("foo", "bard") -> Map("a" -> Rational(2, 10), "b" -> Rational(8, 10)),
        ("food", "bar") -> Map("a" -> Rational(9, 10), "b" -> Rational(1, 10)),
        ("food", "bard") -> Map("a" -> Rational(5, 10), "b" -> Rational(5, 10))))

      val A = RandomVariable2("A", X, Y, cpt)

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

      val biasedCoin = coin(Rational(9, 10))
      val fairCoin = coin()

      // TODO: figure out why equalTo isn't working here
      entropy(biasedCoin).magnitude should be equalTo (0.46899559358928117)
      entropy(fairCoin).magnitude should be equalTo (1.0)
    }
  }

}
