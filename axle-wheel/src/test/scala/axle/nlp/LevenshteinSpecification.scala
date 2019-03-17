package axle.nlp

import org.jblas.DoubleMatrix
import org.scalatest._
import org.typelevel.discipline.Predicate
import org.typelevel.discipline.scalatest.Discipline

import cats.implicits._
import spire.algebra._
import spire.laws.VectorSpaceLaws
import axle.jblas.linearAlgebraDoubleMatrix

class LevenshteinSpecification
  extends FunSuite with Matchers
  with Discipline {

  implicit val ringInt: Ring[Int] = spire.implicits.IntAlgebra

  implicit val laJblasInt = {
    implicit val nrootInt: NRoot[Int] = spire.implicits.IntAlgebra
    linearAlgebraDoubleMatrix[Int]
  }

  implicit val space = Levenshtein[Vector, Char, DoubleMatrix, Int]()

  implicit val pred: Predicate[Int] = Predicate.const[Int](true)

  checkAll(
    "Levenshtein space as MetricSpace[Vector[Char], Int]",
    VectorSpaceLaws[Vector[Char], Int].metricSpace)

}
