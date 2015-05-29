package axle.ml.distance

import scala.annotation.implicitNotFound
import scala.reflect.ClassTag

import org.jblas.DoubleMatrix
import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.scalacheck.Gen.Choose
import org.specs2.mutable.Specification
import org.typelevel.discipline.specs2.mutable.Discipline

import axle.algebra.LinearAlgebra
import axle.algebra.laws.MetricSpaceLaws
import axle.jblas.linearAlgebraDoubleMatrix
import spire.implicits.IntAlgebra

class ManhattanSpec
    extends Specification
    with Discipline {

  implicit val space = Manhattan[DoubleMatrix, Int, Int, Int]()

  val m = 1
  val n = 2

  implicit val laJblasInt = linearAlgebraDoubleMatrix[Int]

  implicit val arbMatrix: Arbitrary[DoubleMatrix] =
    Arbitrary(LinearAlgebra.genMatrix[DoubleMatrix, Int](m, n, -10000, 10000))

  checkAll(s"Manhattan space on ${m}x${n} matrix",
    MetricSpaceLaws[DoubleMatrix, Int].laws)

}