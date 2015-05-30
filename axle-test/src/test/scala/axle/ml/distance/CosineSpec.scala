package axle.ml.distance

import org.jblas.DoubleMatrix
import org.specs2.mutable.Specification
import org.typelevel.discipline.specs2.mutable.Discipline
import org.typelevel.discipline.Predicate
import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import spire.implicits.IntAlgebra
import spire.implicits.DoubleAlgebra
import spire.laws.VectorSpaceLaws
import axle.jblas._
import axle.jblas.eqDoubleMatrix
import axle.algebra.LinearAlgebra

class CosineSpec
    extends Specification
    with Discipline {

  implicit val laJblasDouble = linearAlgebraDoubleMatrix[Double]

  val m = 1
  val n = 2

  implicit val arbMatrix: Arbitrary[DoubleMatrix] =
    Arbitrary(LinearAlgebra.genMatrix[DoubleMatrix, Double](m, n, -100000d, 100000d))

  implicit val space = Cosine[DoubleMatrix, Int, Int, Double](n)

  implicit val pred: Predicate[Double] = new Predicate[Double] {
    def apply(a: Double) = true
  }

  checkAll("Cosine space on 1x2 matrix",
    VectorSpaceLaws[DoubleMatrix, Double].innerProductSpace)

}