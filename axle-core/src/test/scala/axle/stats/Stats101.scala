package axle.stats

import org.specs2.mutable._
import spire.math._
import spire.algebra._
import spire.implicits._

class Stats101 extends Specification {

  "standard deviation" should {
    "work" in {

      val dist = uniformDistribution(List(2d, 4d, 4d, 4d, 5d, 5d, 7d, 9d), "numbers")

      standardDeviation(dist) must be equalTo 2d
    }
  }

}
