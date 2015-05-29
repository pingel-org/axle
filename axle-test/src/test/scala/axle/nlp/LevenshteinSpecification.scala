package axle.nlp

import org.jblas.DoubleMatrix
import org.specs2.mutable.Specification
import org.typelevel.discipline.Predicate
import org.typelevel.discipline.specs2.mutable.Discipline

import axle.jblas.linearAlgebraDoubleMatrix
import spire.algebra.Eq
import spire.implicits.CharAlgebra
import spire.implicits.IntAlgebra
import spire.laws.VectorSpaceLaws

class LevenshteinSpecification
    extends Specification
    with Discipline {

  implicit def eqIsT[T: Eq]: Eq[IndexedSeq[T]] = new Eq[IndexedSeq[T]] {
    def eqv(x: IndexedSeq[T], y: IndexedSeq[T]): Boolean = {
      val lhs = (x.size == y.size)
      val rhs = (x.zip(y).forall({ case (a, b) => a == b }))
      lhs && rhs
    }
  }

  implicit val laJblasInt = linearAlgebraDoubleMatrix[Int]

  implicit val space = Levenshtein[IndexedSeq, Char, DoubleMatrix, Int]()

  implicit val pred: Predicate[Int] = new Predicate[Int] {
    def apply(a: Int) = true
  }

  checkAll("Levenshtein space",
    VectorSpaceLaws[IndexedSeq[Char], Int].metricSpace)

}