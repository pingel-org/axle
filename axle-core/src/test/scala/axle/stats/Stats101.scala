package axle.stats

import org.specs2.mutable._
import spire.math._

class Stats101 extends Specification {

  "standard deviation" should {
    "work" in {
      stddev(Vector(2, 4, 4, 4, 5, 5, 7, 9).map(Real(_))) must be equalTo Real(2)
    }
  }

}