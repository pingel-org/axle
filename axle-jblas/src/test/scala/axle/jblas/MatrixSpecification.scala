package axle.jblas

import org.specs2.mutable._
import axle.jblas.DoubleMatrixWitnesses._
import axle.syntax.linearalgebra._
import axle.syntax.endofunctor._
import axle.syntax.LinearAlgebraOps

class MatrixSpecification extends Specification {

  //implicit val la = axle.jblas.DoubleMatrixWitnesses.linearAlrebraDoubleMatrix

  "DoubleJblasMatrix" should {
    "work" in {

      val z = linearAlrebraDoubleMatrix.zeros(3, 4) // .zero
      val o = linearAlrebraDoubleMatrix.ones(2, 3)
      val r = linearAlrebraDoubleMatrix.rand(1, 2)

      val dm = linearAlrebraDoubleMatrix.rand(3, 3)
      val c2 = dm.column(2)
      val r2 = dm.row(2)

      1 must be equalTo (1)
    }
  }

  "x+x === x.map(*2)" should {
    "work" in {
      val x = linearAlrebraDoubleMatrix.randn(2, 2)

      val xx = x + x
      val mapped = x.map(_ * 2)
      
      xx must be equalTo mapped
    }
  }

  //  "IntJblasMatrix" should {
  //    "work" in {
  //
  //      val z = zeros[Int](1, 3)
  //      val o = ones[Int](2, 2)
  //      val e = eye[Int](3)
  //
  //      1 must be equalTo (1)
  //    }
  //  }

  //  "BooleanJblasMatrix" should {
  //    "work" in {
  //
  //      val f = falses(2, 3)
  //      val t = trues(3, 2)
  //      val e = eye[Boolean](4)
  //
  //      1 must be equalTo (1)
  //    }
  //  }

}

