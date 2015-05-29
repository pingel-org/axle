package axle.ml.distance

import scala.annotation.implicitNotFound

import org.jblas.DoubleMatrix
import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.scalacheck.Gen.Choose
import org.specs2.mutable.Specification
import org.typelevel.discipline.specs2.mutable.Discipline
import org.typelevel.discipline.Predicate

import axle.algebra.LinearAlgebra
import axle.algebra.laws.MetricSpaceLaws
import axle.jblas.linearAlgebraDoubleMatrix
import axle.jblas.eqDoubleMatrix
import spire.implicits.DoubleAlgebra
import spire.implicits.IntAlgebra
import spire.laws.VectorSpaceLaws
import spire.algebra.Eq

class EuclideanSpec
    extends Specification
    with Discipline {

  implicit val laJblasDouble = linearAlgebraDoubleMatrix[Double]

  val m = 1
  val n = 2

  implicit val space = Euclidean[DoubleMatrix, Int, Int, Double](n)

  implicit val arbMatrix: Arbitrary[DoubleMatrix] =
    Arbitrary(LinearAlgebra.genMatrix[DoubleMatrix, Double](m, n, -100000d, 100000d))

  implicit val genDouble = Gen.choose[Double](-100d, 100d)

  implicit val pred: Predicate[Double] = new Predicate[Double] {
    def apply(a: Double) = true
  }

  checkAll("Euclidean space on 1x2 matrix",
    VectorSpaceLaws[DoubleMatrix, Double].metricSpace)
}