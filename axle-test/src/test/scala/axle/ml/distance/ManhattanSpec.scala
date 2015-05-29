package axle.ml.distance

import org.jblas.DoubleMatrix
import org.specs2.mutable.Specification
import org.typelevel.discipline.specs2.mutable.Discipline
import org.scalacheck.Arbitrary
import org.scalacheck.Gen

class ManhattanSpec
    extends Specification
    with Discipline {

  import axle.algebra.laws.MetricSpaceLaws
  import spire.implicits.IntAlgebra
  import axle.jblas._

  implicit val laJblasInt = linearAlgebraDoubleMatrix[Int]

  implicit val space = Manhattan[DoubleMatrix, Int, Int, Int]()

  val genMatrix: Gen[DoubleMatrix] = for {
    x <- Gen.choose(-100000, 1000000)
    y <- Gen.choose(-100000, 1000000)
  } yield laJblasInt.matrix(1, 2, List(x, y).toArray)

  implicit val arbMatrix: Arbitrary[DoubleMatrix] = Arbitrary(genMatrix)

  checkAll("Manhattan space on 1x2 matrix",
      MetricSpaceLaws[DoubleMatrix, Int].laws)

}