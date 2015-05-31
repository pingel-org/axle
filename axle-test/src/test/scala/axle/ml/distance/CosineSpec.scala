package axle.ml.distance

import org.jblas.DoubleMatrix
import org.scalacheck.Arbitrary
import org.specs2.mutable.Specification
import org.typelevel.discipline.Predicate
import org.typelevel.discipline.specs2.mutable.Discipline

import axle.algebra.LinearAlgebra
import axle.jblas.eqDoubleMatrix
import axle.jblas.linearAlgebraDoubleMatrix
import axle.jblas.moduleDoubleMatrix
import spire.implicits.DoubleAlgebra
import spire.implicits.IntAlgebra
import spire.laws.VectorSpaceLaws

class CosineSpec
    extends Specification
    with Discipline {

  implicit val laJblasDouble = linearAlgebraDoubleMatrix[Double]

  val m = 1
  val n = 2

  implicit val ips = axle.jblas.innerProductSpace(n)(IntAlgebra, laJblasDouble, moduleDoubleMatrix)

  implicit val space = Cosine[DoubleMatrix, Double]()

  implicit val pred: Predicate[Double] = new Predicate[Double] {
    def apply(a: Double) = true
  }

  implicit val arbMatrix: Arbitrary[DoubleMatrix] =
    Arbitrary(LinearAlgebra.genMatrix[DoubleMatrix, Double](m, n, 0d, 10d))

  checkAll("Cosine space on 1x2 matrix",
    VectorSpaceLaws[DoubleMatrix, Double].metricSpace)

}