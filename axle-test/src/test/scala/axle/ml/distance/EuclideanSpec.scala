package axle.ml.distance

import org.jblas.DoubleMatrix
import org.specs2.mutable.Specification
import org.typelevel.discipline.specs2.mutable.Discipline
import axle.algebra.Zero
import org.scalacheck.Arbitrary
import org.scalacheck.Gen

class EuclideanSpec
    extends Specification
    with Discipline {

  import axle.algebra.laws.MetricSpaceLaws
  import spire.implicits.IntAlgebra
  import spire.implicits.DoubleAlgebra
  import axle.jblas._

  implicit val laJblasDouble = linearAlgebraDoubleMatrix[Double]

  val n = 2

  implicit val space = Euclidean[DoubleMatrix, Int, Int, Double](n)

  val genMatrix: Gen[DoubleMatrix] = for {
    x <- Gen.choose(-100000d, 1000000d)
    y <- Gen.choose(-100000d, 1000000d)
  } yield laJblasDouble.matrix(1, 2, List(x, y).toArray)

  implicit val arbMatrix: Arbitrary[DoubleMatrix] = Arbitrary(genMatrix)

  checkAll("Euclidean space on 1x2 matrix", MetricSpaceLaws[DoubleMatrix, Double].cauchySchwarz)

}