package axle.algebra.distance

import org.jblas.DoubleMatrix
import org.scalacheck.Arbitrary
import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers
import org.typelevel.discipline.Predicate
import org.typelevel.discipline.scalatest.Discipline

import cats.implicits._

import spire.algebra._
import spire.laws.VectorSpaceLaws

import axle.algebra.metricSpaceFromAdditiveGroupSigned
import axle.algebra.LinearAlgebra
import axle.jblas.linearAlgebraDoubleMatrix
import axle.jblas.eqDoubleMatrix

class ManhattanSpec
  extends AnyFunSuite with Matchers
  with Discipline {

  implicit val additiveGroupInt: AdditiveGroup[Int] = spire.implicits.IntAlgebra
  implicit val ringInt: Ring[Int] = spire.implicits.IntAlgebra
  implicit val mmInt: MultiplicativeMonoid[Int] = spire.implicits.IntAlgebra
  implicit val nrootInt: NRoot[Int] = spire.implicits.IntAlgebra

  implicit val space = {
    implicit val signedInt: Signed[Int] = spire.implicits.IntAlgebra
    // implicit val msDD = metricSpaceFromAdditiveGroupSigned[Int]
    // MetricSpace[Int, Int]
    new Manhattan[DoubleMatrix, Int, Int, Int]()
  }

  val m = 1
  val n = 2

  implicit val laJblasInt = linearAlgebraDoubleMatrix[Int]

  implicit val arbMatrix: Arbitrary[DoubleMatrix] =
    Arbitrary(LinearAlgebra.genMatrix[DoubleMatrix, Int](m, n, -10000, 10000))

  implicit val pred: Predicate[Int] = Predicate.const[Int](true)

  checkAll(
    s"Manhattan space on ${m}x${n} matrix",
    VectorSpaceLaws[DoubleMatrix, Int].metricSpace)

}
