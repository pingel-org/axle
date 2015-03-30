package axle.jblas

import org.specs2.mutable._
import axle.syntax.linearalgebra._
import axle.syntax.endofunctor._
import spire.implicits._

class MatrixSpecification extends Specification {

  "DoubleJblasMatrix" should {
    "work" in {

      val z = linearAlgebraDoubleMatrix.zeros(3, 4) // .zero
      val o = linearAlgebraDoubleMatrix.ones(2, 3)
      val r = linearAlgebraDoubleMatrix.rand(1, 2)

      val dm = linearAlgebraDoubleMatrix.rand(3, 3)
      val c2 = dm.column(2)
      val r2 = dm.row(2)

      1 must be equalTo (1)
    }
  }

  "x+x === x.map(*2)" should {
    "work" in {
      
      implicit val ring = linearAlgebraDoubleMatrix.ring
      
      val x = linearAlgebraDoubleMatrix.randn(2, 2)

      val xx = ring.plus(x, x)
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

