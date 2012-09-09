package axle.stats

import org.specs2.mutable._

import axle.stats._

class ProbabilitySpec extends Specification {

  "coins" should {
    "work" in {

      val coin1 = coin()
      val coin2 = coin()
      P(coin1 eq 'HEAD)() must be equalTo 0.5
      P((coin1 eq 'HEAD) and (coin2 eq 'HEAD))() must be equalTo 0.25
      P((coin1 eq 'HEAD) or (coin2 eq 'HEAD))() must be equalTo 0.75
    }
  }

  "dice" should {
    "work" in {

      val d6a = d6()
      val d6b = d6()
      P(d6a eq '⚃)() must be equalTo 0.16666666666666666
      P((d6a eq '⚃) and (d6b eq '⚃))() must be equalTo 0.027777777777777776
      P((d6a ne '⚃))() must be equalTo 0.8333333333333334
    }
  }

}