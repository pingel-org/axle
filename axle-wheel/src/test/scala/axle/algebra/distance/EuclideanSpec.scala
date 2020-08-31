package axle.algebra.distance

import org.jblas.DoubleMatrix
import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers
import org.typelevel.discipline.Predicate
import org.typelevel.discipline.scalatest.Discipline

import cats.implicits._

import spire.algebra._
import spire.laws.VectorSpaceLaws

import axle.algebra.LinearAlgebra
import axle.jblas.eqDoubleMatrix
import axle.jblas.linearAlgebraDoubleMatrix
import axle.jblas.rowVectorInnerProductSpace

class EuclideanSpec
  extends AnyFunSuite with Matchers
  with Discipline {

  val n = 2

  implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
  implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra
  implicit val mmInt: MultiplicativeMonoid[Int] = spire.implicits.IntAlgebra

  // TODO Double value type
  implicit val innerSpace = rowVectorInnerProductSpace[Int, Int, Double](n)

  implicit val space = new Euclidean[DoubleMatrix, Double]()

  implicit val arbMatrix: Arbitrary[DoubleMatrix] =
    Arbitrary(LinearAlgebra.genMatrix[DoubleMatrix, Double](1, n, -100000d, 100000d))

  implicit val genDouble = Gen.choose[Double](-100d, 100d)

  implicit val pred: Predicate[Double] = Predicate.const[Double](true)

  checkAll(
    s"Euclidean space on 1x${n} matrix",
    VectorSpaceLaws[DoubleMatrix, Double].metricSpace)
}
