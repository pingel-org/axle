package axle.bio

import org.jblas.DoubleMatrix
import org.specs2.mutable.Specification

import axle.jblas.linearAlgebraDoubleMatrix
import spire.implicits.DoubleAlgebra

class AlignDNA extends Specification {

  "Needleman-Wunsch" should {
    "work" in {

      import NeedlemanWunsch.optimalAlignment
      import NeedlemanWunsch.Default._
      import NeedlemanWunsch.Default.charEq
      import NeedlemanWunsch.Default.dim
      import NeedlemanWunsch.Default.gap
      import NeedlemanWunsch.Default.gapPenalty
      import NeedlemanWunsch.Default.intRing
      import NeedlemanWunsch.Default.od
      import NeedlemanWunsch.Default.orderRing
      import NeedlemanWunsch.Default.similarity
      import NeedlemanWunsch.optimalAlignment

      implicit val laJblasDouble = {
        import spire.implicits.DoubleAlgebra
        linearAlgebraDoubleMatrix[Double]
      }

      val (a1, a2) =
        optimalAlignment[IndexedSeq, Char, DoubleMatrix, Int, Double](
          "ATGCGGCC", "ATCGCCGG", similarity, gap, gapPenalty)

      a1.mkString("") must be equalTo "ATGCGGCC--"
      a2.mkString("") must be equalTo "AT-C-GCCGG"
    }
  }

  "Smith-Waterman" should {
    "work" in {

      import spire.implicits.DoubleAlgebra
      implicit val laJblasDouble = linearAlgebraDoubleMatrix[Double]

      SmithWaterman.optimalAlignment("ACACACTA", "AGCACACA") must be equalTo ("A-CACACTA", "AGCACAC-A")
    }
  }

}
