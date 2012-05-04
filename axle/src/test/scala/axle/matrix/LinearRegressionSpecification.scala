package axle.matrix

import org.specs2.mutable._

class LinearRegressionSpecification extends Specification {

  "Linear Regression" should {
    "work" in {

      import JblasMatrixFactory._
      import axle.ml.LinearRegression._

      val y = matrix(4, 1, Array(460.0, 232.0, 315.0, 178.0))

      val examples = matrix(4, 4, Array[Double](
        2104, 5, 1, 45,
        1416, 3, 2, 40,
        1534, 3, 2, 30,
         852, 2, 1, 36
      )).t // fromArray transposes

      val examplesScaled = scaleColumns(examples)
      
      val X: JblasMatrix[Double] = ones[Double](examples.rows, 1) +|+ examplesScaled._1
      
      val yScaled = scaleColumns(y)
      val θ0 = ones[Double](X.columns, 1)
      val α = 0.1
      val N = 100 // iterations
      
      val θ = gradientDescentMutable(X, yScaled._1, θ0, α, N)

      // TODO: an h that incorporates the scaling that was applied in X and y

      1 must be equalTo (1)
    }
  }

}
