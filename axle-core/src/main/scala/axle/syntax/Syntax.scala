package axle.syntax

import axle.algebra.Matrix

trait MatrixSyntax {

  implicit def matrixOps[M[_]: Matrix, A](ma: M[A]) = new MatrixOps(ma)
}
