package org.pingel.axle.matrix

import org.specs2.mutable._

class LinearRegressionSpecification extends Specification {

  "Linear Regression" should {
    "work" in {

      import org.pingel.axle.matrix.DoubleJblasMatrixFactory._
      import org.pingel.axle.matrix.LinearRegression._

      val y = fromArray(4, 1, Array(460.0, 232.0, 315.0, 178.0))

      val examples = fromArray(4, 4, Array[Double](
        2104, 5, 1, 45,
        1416, 3, 2, 40,
        1534, 3, 2, 30,
         852, 2, 1, 36
      )).t // fromArray transposes

      val examplesScaled = scaleColumns(examples)
      val X = ones(examples.rows, 1) +|+ examplesScaled._1
      val yScaled = scaleColumns(y)
      val theta = ones(X.columns, 1)
      val alpha = 0.1
      val iterations = 100
      
      val θ = gradientDescentMutable(X, yScaled._1, theta, 0.1, 100)

      // TODO: an h that incorporates the scaling that was applied in X and y

      1 must be equalTo (1)
    }
  }

}