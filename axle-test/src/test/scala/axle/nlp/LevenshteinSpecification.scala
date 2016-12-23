package axle.nlp

import org.jblas.DoubleMatrix
import org.scalatest._
import org.typelevel.discipline.Predicate
import org.typelevel.discipline.scalatest.Discipline

import axle.jblas.linearAlgebraDoubleMatrix
import spire.laws.VectorSpaceLaws
import spire.implicits._
import cats.implicits._

class LevenshteinSpecification
    extends FunSuite with Matchers
    with Discipline {

  implicit val laJblasInt = linearAlgebraDoubleMatrix[Int]

  // implicit val space = Levenshtein[IndexedSeq[Char], Char, DoubleMatrix, Int]()

  implicit val space = Levenshtein.common[IndexedSeq, Char, DoubleMatrix, Int]()

  implicit val pred: Predicate[Int] = new Predicate[Int] {
    def apply(a: Int) = true
  }

  checkAll("Levenshtein space",
    VectorSpaceLaws[IndexedSeq[Char], Int].metricSpace)

}
