package axle.ml.distance

import scala.annotation.implicitNotFound

import org.jblas.DoubleMatrix
import org.scalacheck.Arbitrary
import org.specs2.mutable.Specification
import org.typelevel.discipline.specs2.mutable.Discipline

import axle.algebra.LinearAlgebra
import axle.algebra.laws.MetricSpaceLaws
import axle.jblas.linearAlgebraDoubleMatrix
import spire.implicits.DoubleAlgebra
import spire.implicits.IntAlgebra

class EuclideanSpec
    extends Specification
    with Discipline {

  implicit val laJblasDouble = linearAlgebraDoubleMatrix[Double]

  val m = 1
  val n = 2

  implicit val space = Euclidean[DoubleMatrix, Int, Int, Double](n)

  implicit val arbMatrix: Arbitrary[DoubleMatrix] =
    Arbitrary(LinearAlgebra.genMatrix[DoubleMatrix, Double](m, n, -100000d, 100000d))

  checkAll("Euclidean space on 1x2 matrix",
    MetricSpaceLaws[DoubleMatrix, Double].laws)

  // TODO use laws from spire
}