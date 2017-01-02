package axle.ml.distance

import org.jblas.DoubleMatrix
import org.scalacheck.Arbitrary
import org.scalatest._
import org.typelevel.discipline.Predicate
import org.typelevel.discipline.scalatest.Discipline

import axle.algebra.LinearAlgebra
import axle.jblas.linearAlgebraDoubleMatrix
import axle.jblas.eqDoubleMatrix
import spire.implicits.IntAlgebra
import spire.laws.VectorSpaceLaws

class ManhattanSpec
    extends FunSuite with Matchers
    with Discipline {

  implicit val space = Manhattan[DoubleMatrix, Int, Int, Int]()

  val m = 1
  val n = 2

  implicit val laJblasInt = linearAlgebraDoubleMatrix[Int]

  implicit val arbMatrix: Arbitrary[DoubleMatrix] =
    Arbitrary(LinearAlgebra.genMatrix[DoubleMatrix, Int](m, n, -10000, 10000))

  implicit val pred: Predicate[Int] = new Predicate[Int] {
    def apply(a: Int) = true
  }

  // TODO spire conversion
  implicit val spireEqDoubleMatrix =
    new spire.algebra.Eq[DoubleMatrix] {
      val catsEq = cats.kernel.Eq[DoubleMatrix]
      def eqv(x: DoubleMatrix, y: DoubleMatrix): Boolean = catsEq.eqv(x, y)
    }

  checkAll(s"Manhattan space on ${m}x${n} matrix",
    VectorSpaceLaws[DoubleMatrix, Int].metricSpace)

}
