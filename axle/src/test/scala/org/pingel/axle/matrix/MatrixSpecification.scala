package org.pingel.axle.matrix

import org.specs2.mutable._

class MatrixSpecification extends Specification {

  import org.pingel.axle.matrix._

  "DoubleMatrix" should {
    "work" in {

      DoubleMatrixFactory.zeros(3, 4)
      DoubleMatrixFactory.ones(2, 3)
      DoubleMatrixFactory.rand(1, 2)
      DoubleMatrixFactory.randn(2, 2)

      val dm = DoubleMatrixFactory.rand(3, 3)
      val c2 = dm.getColumn(2)
      val r2 = dm.getRow(2)

      1 must be equalTo (1)
    }
  }

  "IntMatrix" should {
    "work" in {

      IntMatrixFactory.zeros(1, 3)
      IntMatrixFactory.ones(2, 2)
      IntMatrixFactory.eye(3)

      1 must be equalTo (1)
    }
  }

  "BooleanMatrix" should {
    "work" in {

      BooleanMatrixFactory.falses(2, 3)
      BooleanMatrixFactory.trues(3, 2)
      BooleanMatrixFactory.eye(4)

      1 must be equalTo (1)
    }
  }

  "SetMatrix" should {
    "work" in {

      SetMatrixFactory.zeros[Int](3, 3)

      1 must be equalTo (1)
    }
  }

}

