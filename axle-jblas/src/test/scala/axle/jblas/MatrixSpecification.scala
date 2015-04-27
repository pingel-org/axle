package axle.jblas

import org.jblas.DoubleMatrix
import org.specs2.mutable.Specification

class MatrixSpecification extends Specification {

  "DoubleJblasMatrix" should {
    "work" in {

      import axle.syntax.endofunctor.endofunctorOps
      import axle.syntax.linearalgebra.matrixOps
      import axle.jblas.linearAlgebraDoubleMatrix.{ ones, zeros, rand }

      val z = zeros(3, 4)
      val o = ones(2, 3)
      val x = rand(1, 2)

      val y = rand(3, 3)
      val c = y.column(2)
      val r = y.row(2)

      1 must be equalTo (1)
    }
  }

  "x+x === x.map(_*2)" should {
    "work" in {

      import linearAlgebraDoubleMatrix.{ randn }
      import axle.syntax.endofunctor.endofunctorOps
      import axle.syntax.linearalgebra.matrixOps

      val x = randn(2, 2)

      val xx = implicitly[spire.algebra.Ring[DoubleMatrix]].plus(x, x)

      val mapped = x.map(_ * 2)

      xx must be equalTo mapped
    }
  }

  "aside, atop, transpose" should {
    "work" in {

      import axle.jblas.linearAlgebraDoubleMatrix.rand
      import axle.syntax.linearalgebra.matrixOps

      val r = 2
      val c = 3

      val m = rand(r, c)
      val n = rand(r, c)

      (n atop m) must be equalTo ((n.t aside m.t).t)
    }
  }

}

