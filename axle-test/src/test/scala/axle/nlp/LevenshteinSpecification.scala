package axle.nlp

import org.jblas.DoubleMatrix
import org.specs2.mutable.Specification
import org.typelevel.discipline.Predicate
import org.typelevel.discipline.specs2.mutable.Discipline

import axle.eqIndexedSeq
import axle.jblas.linearAlgebraDoubleMatrix
import spire.algebra.Eq
import spire.implicits.CharAlgebra
import spire.implicits.IntAlgebra
import spire.laws.VectorSpaceLaws

class LevenshteinSpecification
    extends Specification
    with Discipline {

  implicit val laJblasInt = linearAlgebraDoubleMatrix[Int]

  implicit val space = Levenshtein[IndexedSeq[Char], Char, DoubleMatrix, Int]()

  implicit val pred: Predicate[Int] = new Predicate[Int] {
    def apply(a: Int) = true
  }

  checkAll("Levenshtein space",
    VectorSpaceLaws[IndexedSeq[Char], Int].metricSpace)

}