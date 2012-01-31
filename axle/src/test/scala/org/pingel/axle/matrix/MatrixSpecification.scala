package org.pingel.axle.matrix

import org.specs2.mutable._

class MatrixSpecification extends Specification {

  "JblasMatrix[Double]" should {
    "work" in {

      import org.pingel.axle.matrix.DoubleJblasMatrixFactory._

      val z: MatrixIntf[Double] = zeros(3, 4)
      val o = ones(2, 3)
      val r = rand(1, 2)
      val rn = randn(2, 2)

      val dm = rand(3, 3)
      val c2 = dm.getColumn(2)
      val r2 = dm.getRow(2)

      1 must be equalTo (1)
    }
  }

  "JblasMatrix[Int]" should {
    "work" in {

      import org.pingel.axle.matrix.IntJblasMatrixFactory._

      val z: MatrixIntf[Int] = zeros(1, 3)
      val o = ones(2, 2)
      val e = eye(3)

      1 must be equalTo (1)
    }
  }

  "JblasMatrix[Boolean]" should {
    "work" in {

      import org.pingel.axle.matrix.BooleanJblasMatrixFactory._

      val f: MatrixIntf[Boolean] = falses(2, 3)
      val t = trues(3, 2)
      val e = eye(4)

      1 must be equalTo (1)
    }
  }

  "SetMatrix" should {
    "work" in {

      // SetMatrixFactory.zeros[Int](3, 3)

      1 must be equalTo (1)
    }
  }

}

