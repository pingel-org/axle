package axle

import org.specs2.mutable.Specification

import axle.stats.ConditionalProbabilityTable0
import axle.stats.ConditionalProbabilityTable2
import axle.stats.P
import axle.stats.coin
import axle.stats.entropy
import axle.stats.rationalProbabilityDist
import axle.jung.JungDirectedGraph.directedGraphJung // conversion graph
import spire.algebra.Order
import spire.math.Number.apply
import spire.math.Rational
import spire.math.Real
import spire.implicits._

class InformationTheorySpec extends Specification {

  "hard-coded distributions" should {

    "work" in {

      val d =
        new ConditionalProbabilityTable0(Map(
          "A" -> Rational(2, 10),
          "B" -> Rational(1, 10),
          "C" -> Rational(7, 10)), "d")

      entropy(d).magnitude.toDouble must be equalTo (1.1567796494470395)
    }
  }

  "cpt" should {
    "work" in {

      val X = new ConditionalProbabilityTable0(Map(
        "foo" -> Rational(1, 10),
        "food" -> Rational(9, 10)), "X")

      val Y = new ConditionalProbabilityTable0(Map(
        "bar" -> Rational(9, 10),
        "bard" -> Rational(1, 10)), "Y")

      // Note: A is given X and Y
      val A = new ConditionalProbabilityTable2(Map(
        ("foo", "bar") -> Map("a" -> Rational(3, 10), "b" -> Rational(7, 10)),
        ("foo", "bard") -> Map("a" -> Rational(2, 10), "b" -> Rational(8, 10)),
        ("food", "bar") -> Map("a" -> Rational(9, 10), "b" -> Rational(1, 10)),
        ("food", "bard") -> Map("a" -> Rational(5, 10), "b" -> Rational(5, 10))),
        "A")

      val p = P((A is "a") | (X is "foo") ∧ (Y isnt "bar"))
      val b = P((A is "a") ∧ (X is "foo")).bayes

      // TODO
      1 should be equalTo (1)
    }
  }

  "coins" should {
    "work" in {

      val biasedCoin = coin(Rational(9, 10))
      val fairCoin = coin()

      // TODO: figure out why equalTo isn't working here
      entropy(biasedCoin).magnitude.toDouble should be equalTo (0.4689955935892812)
      entropy(fairCoin).magnitude.toDouble should be equalTo (1.0)
    }
  }

}
