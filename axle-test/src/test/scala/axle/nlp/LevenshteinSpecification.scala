package axle.nlp

import org.specs2.mutable.Specification
import org.typelevel.discipline.specs2.mutable.Discipline

import axle._
import axle.algebra._
import spire.implicits.IntAlgebra
import org.jblas.DoubleMatrix
import axle.jblas._
import axle.algebra.laws.MetricSpaceLaws

class LevenshteinSpecification
  extends Specification
  with Discipline {

  implicit val laJblasInt = linearAlgebraDoubleMatrix[Int]

  implicit val space = Levenshtein[IndexedSeq, Char, DoubleMatrix, Int]()

  checkAll("Levenshtein space", MetricSpaceLaws[IndexedSeq[Char], Int].laws)

}