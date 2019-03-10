package axle.ml

import org.scalatest._

import spire.algebra._

import axle.jblas._

class LinearRegressionSpecification
  extends FunSuite with Matchers {

  test("Linear Regression") {

    case class RealtyListing(size: Double, bedrooms: Int, floors: Int, age: Int, price: Double)

    val data =
      RealtyListing(2104, 5, 1, 45, 460.0) ::
        RealtyListing(1416, 3, 2, 40, 232.0) ::
        RealtyListing(1534, 3, 2, 30, 315.0) ::
        RealtyListing(852, 2, 1, 36, 178.0) ::
        Nil

      implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
      implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra
      implicit val laJblasDouble = linearAlgebraDoubleMatrix[Double]

    val priceEstimator = LinearRegression(
      data,
      4,
      (rl: RealtyListing) => (rl.size :: rl.bedrooms.toDouble :: rl.floors.toDouble :: rl.age.toDouble :: Nil),
      (rl: RealtyListing) => rl.price,
      0.1,
      10)

    val priceGuess = priceEstimator(RealtyListing(1416, 3, 2, 40, 0.0))

    priceGuess should be(412.6509523494042)
  }

}
