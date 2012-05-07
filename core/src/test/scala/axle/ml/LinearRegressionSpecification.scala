package axle.ml

import org.specs2.mutable._

class LinearRegressionSpecification extends Specification {

  "Linear Regression" should {
    "work" in {

      import axle.matrix.JblasMatrixFactory._
      import LinearRegression._

      case class RealtyListing(size: Double, bedrooms: Int, floors: Int, age: Int, price: Double)

      val data =
        RealtyListing(2104, 5, 1, 45, 460.0) ::
          RealtyListing(1416, 3, 2, 40, 232.0) ::
          RealtyListing(1534, 3, 2, 30, 315.0) ::
          RealtyListing(852, 2, 1, 36, 178.0) ::
          Nil

      val theta = regression(
        data,
        4,
        (rl: RealtyListing) => (rl.size :: rl.bedrooms.toDouble :: rl.floors.toDouble :: rl.age.toDouble :: Nil),
        (rl: RealtyListing) => rl.price)

      // TODO: incorporate the scaling that was applied in X and y

      1 must be equalTo (1)
    }
  }

}
