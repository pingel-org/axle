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

}

