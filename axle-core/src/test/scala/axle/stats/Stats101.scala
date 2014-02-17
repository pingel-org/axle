package axle.stats

import org.specs2.mutable._
import spire.implicits._

class Stats101 extends Specification {

  "standard deviation" should {
    "work" in {
      stddev(List(2d, 4d, 4d, 4d, 5d, 5d, 7d, 9d)) must be equalTo 2d
    }
  }

}
