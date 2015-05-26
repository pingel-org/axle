package axle.bio

import org.jblas.DoubleMatrix
import org.specs2.mutable.Specification

class AlignDNA extends Specification {

  "Needleman-Wunsch" should {
    "work" in {

      import NeedlemanWunsch.optimalAlignment
      import NeedlemanWunsch.Default._

      implicit val laJblasDouble = {
        import spire.implicits.DoubleAlgebra
        axle.jblas.linearAlgebraDoubleMatrix[Double]
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

      import SmithWaterman.Default._
      import SmithWaterman.optimalAlignment

      import spire.implicits.DoubleAlgebra
      implicit val laJblasDouble = axle.jblas.linearAlgebraDoubleMatrix[Double]

      val (alignA, alignB) = optimalAlignment("ACACACTA", "AGCACACA", w, mismatchPenalty, gap)

      alignA must be equalTo "A-CACACTA"
      alignB must be equalTo "AGCACAC-A"
    }
  }

}
