package axle.stats

import org.specs2.mutable._

class RootMeanSquareDeviationSpec extends Specification {

  "RMSD" should {
    "work" in {

      import spire.implicits.DoubleAlgebra

      val rmsd = rootMeanSquareDeviation(
        (1 to 4).map(_.toDouble).toList,
        (x: Double) => x,
        (x: Double) => x + 0.2)

      rmsd must be equalTo 0.4000000000000002
    }
  }

}