package axle.stats

import org.specs2.mutable._
import spire.math._
import spire.algebra._
import spire.implicits._

class Stats101 extends Specification {

  "standard deviation" should {

    "work on a list of doubles" in {

      val dist = uniformDistribution(List(2d, 4d, 4d, 4d, 5d, 5d, 7d, 9d), "some doubles")

      standardDeviation(dist) must be equalTo 2d
    }

    "work on a list of reals" in {

      val dist = uniformDistribution(List[Real](2, 4, 4, 4, 5, 5, 7, 9), "some reals")

      standardDeviation(dist) must be equalTo 2
    }
  }

}
