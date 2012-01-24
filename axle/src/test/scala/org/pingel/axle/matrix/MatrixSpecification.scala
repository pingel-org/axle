package org.pingel.axle.matrix

import org.specs2.mutable._

class MatrixSpecification extends Specification {

  import org.pingel.axle.matrix._

  "DoubleMatrix" should {
    "work" in {

      DoubleMatrix.zeros(3, 4)
      DoubleMatrix.ones(2, 3)
      DoubleMatrix.rand(1, 2)
      DoubleMatrix.randn(2, 2)

      val dm = DoubleMatrix.rand(3, 3)
      val c2 = dm.getColumn(2)
      val r2 = dm.getRow(2)

      1 must be equalTo (1)
    }
  }

  "IntMatrix" should {
    "work" in {

      IntMatrix.zeros(1, 3)
      IntMatrix.ones(2, 2)
      IntMatrix.eye(3)

      1 must be equalTo (1)
    }
  }

  "BooleanMatrix" should {
    "work" in {

      BooleanMatrix.falses(2, 3)
      BooleanMatrix.trues(3, 2)
      BooleanMatrix.eye(4)

      1 must be equalTo (1)
    }
  }

  "SetMatrix" should {
    "work" in {

      SetMatrix.zeros[Int](3, 3)

      1 must be equalTo (1)
    }
  }

}

