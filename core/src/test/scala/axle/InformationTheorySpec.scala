package axle

import org.specs2.mutable._

class InformationTheorySpec extends Specification {

  import InformationTheory._
  import quanta.Information._

  "hard-coded distributions" should {

    "work" in {

      val d = distribution(Map("A" -> 0.2, "B" -> 0.1, "C" -> 0.7))

      d.entropy().conversion.get.getPayload must be equalTo (1.1567796494470395)
    }
  }

  "cpt" should {
    "work" in {

      val X = RandomVariable("X", distribution(Map("foo" -> 0.1, "food" -> 0.9)))

      val Y = RandomVariable("Y", distribution(Map("bar" -> 0.9, "bard" -> 0.1)))

      val A = RandomVariable("A",
        cpt(X, Y, Set("a", "b"),
          Map(
            ("foo", "bar") -> Map("a" -> 0.3, "b" -> 0.7),
            ("foo", "bard") -> Map("a" -> 0.2, "b" -> 0.8),
            ("food", "bar") -> Map("a" -> 0.9, "b" -> 0.1),
            ("food", "bard") -> Map("a" -> 0.5, "b" -> 0.5)
          )
        )
      )

      val p = P((A == "a") | (X == "foo") ∧ (Y != "bar"))
      val b = bayes(P((A == "a") ∧ (X == "foo")))

      //      println("p = " + p)
      //      println("p() = " + p())
      //      println("b = " + b)
      //      println("b() = " + b())

      // TODO
      1 should be equalTo (1)
    }
  }

  "coins" should {
    "work" in {

      val biasedCoin = coin(0.9)
      val fairCoin = coin()

      // TODO: figure out why equalTo isn't working here
      biasedCoin.entropy().conversion.get.getPayload should be equalTo (0.46899559358928117)
      fairCoin.entropy().conversion.get.getPayload should be equalTo (1.0)
    }
  }

}