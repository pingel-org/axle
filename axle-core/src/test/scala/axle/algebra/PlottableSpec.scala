package axle.algebra

import axle.algebra.Plottable.doublePlottable
import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers

class PlottableSpec extends AnyFunSuite with Matchers {

  test("Plottable[Double]") {

    doublePlottable.isPlottable(1d) should be(true)
  }

}
