package axle.algebra

import axle.algebra.Plottable.doublePlottable
import org.specs2.mutable.Specification

class PlottableSpec extends Specification {

  "Plottable[Double]" should {
    "work" in {

      doublePlottable.isPlottable(1d) must be equalTo true
    }
  }

}