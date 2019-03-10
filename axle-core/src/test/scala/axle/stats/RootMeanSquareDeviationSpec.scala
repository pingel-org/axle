package axle.stats

import org.scalatest._

import cats.implicits._
import spire.algebra._

class RootMeanSquareDeviationSpec extends FunSuite with Matchers {

  implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
  implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra

  test("RMSD") {

    val rmsd = rootMeanSquareDeviation(
      (1 to 4).map(_.toDouble).toList,
      (x: Double) => x + 0.2)

    rmsd should be(0.4000000000000002)
  }

}
