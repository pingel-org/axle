package axle.stats

import org.scalatest._

class RootMeanSquareDeviationSpec extends FunSuite with Matchers {

  test("RMSD") {

    import spire.implicits.DoubleAlgebra

    val rmsd = rootMeanSquareDeviation(
      (1 to 4).map(_.toDouble).toList,
      (x: Double) => x + 0.2)

    rmsd should be(0.4000000000000002)
  }

}
