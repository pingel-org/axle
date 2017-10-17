package axle

import org.scalatest._

import edu.uci.ics.jung.graph.DirectedSparseGraph
import spire.math.Rational
import spire.implicits._
import axle.stats.ConditionalProbabilityTable0
import axle.stats.ConditionalProbabilityTable2
// import axle.stats.P
import axle.stats.coin
import axle.stats.entropy
// import axle.stats.rationalProbabilityDist
import axle.stats.Variable
import axle.quanta.Information
import axle.jung.directedGraphJung
// import cats.implicits._

class InformationTheorySpec extends FunSuite with Matchers {

  test("hard-coded distributions") {

    implicit val id = Information.converterGraphK2[Double, DirectedSparseGraph]

    val d =
      ConditionalProbabilityTable0(Map(
        "A" -> Rational(2, 10),
        "B" -> Rational(1, 10),
        "C" -> Rational(7, 10)), Variable[String]("d"))

    entropy(d).magnitude should be(1.1567796494470395)
  }

  test("cpt") {

    val X = ConditionalProbabilityTable0(Map(
      "foo" -> Rational(1, 10),
      "food" -> Rational(9, 10)), Variable[String]("X"))

    val Y = ConditionalProbabilityTable0(Map(
      "bar" -> Rational(9, 10),
      "bard" -> Rational(1, 10)), Variable[String]("Y"))

    // Note: A is given X and Y
    val A = ConditionalProbabilityTable2(Map(
      ("foo", "bar") -> Map("a" -> Rational(3, 10), "b" -> Rational(7, 10)),
      ("foo", "bard") -> Map("a" -> Rational(2, 10), "b" -> Rational(8, 10)),
      ("food", "bar") -> Map("a" -> Rational(9, 10), "b" -> Rational(1, 10)),
      ("food", "bard") -> Map("a" -> Rational(5, 10), "b" -> Rational(5, 10))),
      Variable[String]("A"))

    //val p = P((A is "a") | (X is "foo") ∧ (Y isnt "bar"))
    //val b = P((A is "a") ∧ (X is "foo")).bayes

    // TODO
    1 should be(1)
  }

  test("coins") {

    val biasedCoin = coin(Rational(9, 10))
    val fairCoin = coin()

    implicit val id = Information.converterGraphK2[Double, DirectedSparseGraph]

    // assumes entropy is in bits
    entropy(biasedCoin).magnitude should be(0.4689955935892812)
    entropy(fairCoin).magnitude should be(1d)
  }

}
