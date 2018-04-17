package axle.algebra.distance

import org.jblas.DoubleMatrix
import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.scalatest._
import org.typelevel.discipline.Predicate
import org.typelevel.discipline.scalatest.Discipline

import axle.algebra.LinearAlgebra
import axle.jblas.eqDoubleMatrix
import axle.jblas.linearAlgebraDoubleMatrix
import axle.jblas.rowVectorInnerProductSpace
import spire.implicits.DoubleAlgebra
import spire.implicits.IntAlgebra
import spire.laws.VectorSpaceLaws

class EuclideanSpec
  extends FunSuite with Matchers
  with Discipline {

  val n = 2

  // TODO Double value type
  implicit val innerSpace = rowVectorInnerProductSpace[Int, Int, Double](n)

  implicit val space = new Euclidean[DoubleMatrix, Double]()

  implicit val arbMatrix: Arbitrary[DoubleMatrix] =
    Arbitrary(LinearAlgebra.genMatrix[DoubleMatrix, Double](1, n, -100000d, 100000d))

  implicit val genDouble = Gen.choose[Double](-100d, 100d)

  implicit val pred: Predicate[Double] = new Predicate[Double] {
    def apply(a: Double) = true
  }

  checkAll(
    s"Euclidean space on 1x${n} matrix",
    VectorSpaceLaws[DoubleMatrix, Double].metricSpace)
}
