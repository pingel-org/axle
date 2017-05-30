package axle.nlp

import org.jblas.DoubleMatrix
import org.scalatest._
import org.typelevel.discipline.Predicate
import org.typelevel.discipline.scalatest.Discipline
import cats.implicits._
import spire.laws.VectorSpaceLaws
import spire.implicits.IntAlgebra
import axle.jblas.linearAlgebraDoubleMatrix

class LevenshteinSpecification
    extends FunSuite with Matchers
    with Discipline {

  implicit val laJblasInt = linearAlgebraDoubleMatrix[Int]

  // implicit val space = Levenshtein[Vector[Char], Char, DoubleMatrix, Int]()

  implicit val space = Levenshtein.common[Vector, Char, DoubleMatrix, Int]()

  implicit val pred: Predicate[Int] = new Predicate[Int] {
    def apply(a: Int) = true
  }

  checkAll("Levenshtein space",
    VectorSpaceLaws[Vector[Char], Int].metricSpace)

}
