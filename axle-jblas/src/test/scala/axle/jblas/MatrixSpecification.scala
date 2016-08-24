package axle.jblas

import org.jblas.DoubleMatrix
import org.specs2.mutable.Specification
import spire.algebra.Ring

class MatrixSpecification extends Specification {

  "DoubleJblasMatrix" should {
    "work" in {

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

      1 must be equalTo (1)
    }
  }

  "x+x === x.map(_*2)" should {
    "work" in {

      import axle.syntax.endofunctor.endofunctorOps
      import spire.implicits.DoubleAlgebra

      implicit val laJblasDouble = linearAlgebraDoubleMatrix[Double]
      import laJblasDouble.randn

      val x = randn(2, 2)

      val xx = implicitly[Ring[DoubleMatrix]].plus(x, x) // TODO clean up

      implicit val endo = endoFunctorDoubleMatrix[Double] // TODO remove

      val mapped = x.map(_ * 2d)

      xx must be equalTo mapped
    }
  }

  "aside, atop, transpose" should {
    "work" in {

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

