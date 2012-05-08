package axle.ml

import org.specs2.mutable._

class LinearRegressionSpecification extends Specification {

  "Linear Regression" should {
    "work" in {

      import LinearRegression._

      case class RealtyListing(size: Double, bedrooms: Int, floors: Int, age: Int, price: Double)

      val data =
        RealtyListing(2104, 5, 1, 45, 460.0) ::
          RealtyListing(1416, 3, 2, 40, 232.0) ::
          RealtyListing(1534, 3, 2, 30, 315.0) ::
          RealtyListing(852, 2, 1, 36, 178.0) ::
          Nil

      val estimator = regression(
        data,
        4,
        (rl: RealtyListing) => (rl.size :: rl.bedrooms.toDouble :: rl.floors.toDouble :: rl.age.toDouble :: Nil),
        (rl: RealtyListing) => rl.price,
        0.1,
        10)

      val unknown = RealtyListing(1416, 3, 2, 40, 0.0)

      val priceGuess = estimator.estimate(unknown)
      
      priceGuess must be equalTo (412.6509523494042)
    }
  }

}
