package axle.jblas

import org.jblas.DoubleMatrix
import org.specs2.mutable.Specification
import spire.algebra.Ring

class MatrixSpecification extends Specification {

  "DoubleJblasMatrix" should {
    "create simple matries" in {

      import axle.syntax.linearalgebra.matrixOps
      import spire.implicits.DoubleAlgebra

      implicit val dm = linearAlgebraDoubleMatrix[Double]
      import dm.{ ones, zeros, rand }

      val z = zeros(3, 4)
      val o = ones(2, 3)
      val x = rand(1, 2)

      val y = rand(3, 3)
      val c = y.column(2)
      val r = y.row(2)

      z.rows must be equalTo 3
      z.columns must be equalTo 4
    }
  }

  "x+x === x.map(_*2)" should {
    "hold for random 2x2 matrix x" in {

      import axle.syntax.endofunctor.endofunctorOps
      import spire.implicits.DoubleAlgebra
      implicit val laJblasDouble = linearAlgebraDoubleMatrix[Double]
      import laJblasDouble.randn
      implicit val endo = endoFunctorDoubleMatrix[Double] // TODO remove

      val x = randn(2, 2)

      implicitly[Ring[DoubleMatrix]].plus(x, x) must be equalTo x.map(_ * 2d)
    }
  }

  "(n atop m) === (n.t aside m.t).t" should {
    "hold for random 2x3 matrices m and n" in {

      import axle.syntax.linearalgebra.matrixOps
      import spire.implicits.DoubleAlgebra
      implicit val laJblasDouble = linearAlgebraDoubleMatrix[Double]
      import laJblasDouble.rand

      val r = 2
      val c = 3

      val m = rand(r, c)
      val n = rand(r, c)

      (n atop m) must be equalTo ((n.t aside m.t).t)
    }
  }

  "boolean tests" should {
    "return false for random 2x3 matrix" in {

      import axle.syntax.LinearAlgebraOps
      import spire.implicits.DoubleAlgebra
      implicit val laJblasDouble = linearAlgebraDoubleMatrix[Double]
      import laJblasDouble.rand

      // mask raw matrix to ensure Axle's methods are being tested
      val m = new LinearAlgebraOps(rand(2, 3))

      m.isEmpty must be equalTo false
      m.isColumnVector must be equalTo false
      m.isRowVector must be equalTo false
      m.isVector must be equalTo false
      m.isSquare must be equalTo false
      m.isScalar must be equalTo false
    }
  }

  "ceil, floor, log, log10" should {
    "transform a 2x3 matrix" in {

      import axle.syntax.LinearAlgebraOps
      import spire.implicits.DoubleAlgebra
      implicit val laJblasDouble = linearAlgebraDoubleMatrix[Double]
      import laJblasDouble.matrix
      import axle.syntax.linearalgebra._

      // mask raw matrix to ensure Axle's methods are being tested
      val m = new LinearAlgebraOps(matrix(2, 3,
        Array(
          1.4, 22d, 17.5,
          2.3, 18d, 105d)))

      m.ceil must be equalTo matrix(2, 3,
        Array(
          2d, 22d, 18d,
          3d, 18d, 105d))

      m.floor must be equalTo matrix(2, 3,
        Array(
          1d, 22d, 17d,
          2d, 18d, 105d))

      m.log.floor must be equalTo matrix(2, 3,
        Array(
          0d, 3d, 2d,
          0d, 2d, 4d))

      m.log10.floor must be equalTo matrix(2, 3,
        Array(
          0d, 1d, 1d,
          0d, 1d, 2d))
    }
  }

}

