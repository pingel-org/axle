package org.pingel.axle.matrix

import org.specs2.mutable._

class MatrixSpecification extends Specification {

  "DoubleJblasMatrix" should {
    "work" in {

      import org.pingel.axle.matrix.JblasMatrixFactory._

      val z: JblasMatrix[Double] = zeros[Double](3, 4, double2double)
      val o = ones[Double](2, 3, double2double)
      val r = rand[Double](1, 2, double2double)
      val rn = randn[Double](2, 2, double2double)

      val dm = rand[Double](3, 3, double2double)
      val c2 = dm.getColumn(2)
      val r2 = dm.getRow(2)

      1 must be equalTo (1)
    }
  }

  "IntJblasMatrix" should {
    "work" in {

      import org.pingel.axle.matrix.JblasMatrixFactory._

      val z: JblasMatrix[Int] = zeros[Int](1, 3, double2int)
      val o = ones[Int](2, 2, double2int)
      val e = eye[Int](3, double2int)

      1 must be equalTo (1)
    }
  }

  "BooleanJblasMatrix" should {
    "work" in {

      import org.pingel.axle.matrix.JblasMatrixFactory._

      val f: JblasMatrix[Boolean] = zeros[Boolean](2, 3, double2boolean) // TODO: "falses"
      val t = ones[Boolean](3, 2, double2boolean) // TODO: "trues"
      val e = eye[Boolean](4, double2boolean)

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

