package axle.algebra

import axle.algebra.Plottable.doublePlottable
import org.scalatest._

class PlottableSpec extends FunSuite with Matchers {

  test("Plottable[Double]") {

    doublePlottable.isPlottable(1d) should be(true)
  }

}
