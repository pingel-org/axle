package axle.bio

import org.jblas.DoubleMatrix
import org.specs2.mutable.Specification

class AlignDNA extends Specification {

  "Needleman-Wunsch" should {
    "work" in {

      import NeedlemanWunsch.alignmentScore
      import NeedlemanWunsch.optimalAlignment
      import NeedlemanWunsch.Default._

      implicit val laJblasDouble = {
        import spire.implicits.DoubleAlgebra
        axle.jblas.linearAlgebraDoubleMatrix[Double]
      }

      val dna1 = "ATGCGGCC"
      val dna2 = "ATCGCCGG"
      val bestAlignment = ("ATGCGGCC--".toIndexedSeq, "AT-C-GCCGG".toIndexedSeq)

      val nwAlignment =
        optimalAlignment[IndexedSeq, Char, DoubleMatrix, Int, Double](
          dna1, dna2, similarity, gap, gapPenalty)

      val score = alignmentScore(nwAlignment._1, nwAlignment._2, gap, similarity, gapPenalty)

      val space = NeedlemanWunschMetricSpace[IndexedSeq, Char, DoubleMatrix, Int, Double](similarity, gapPenalty)

      nwAlignment must be equalTo bestAlignment
      score must be equalTo 32d
      space.distance(dna1, dna2) must be equalTo score
    }
  }

  "Smith-Waterman" should {
    "work" in {

      import SmithWaterman.Default._
      import SmithWaterman.optimalAlignment

      import spire.implicits.IntAlgebra
      implicit val laJblasInt = axle.jblas.linearAlgebraDoubleMatrix[Int]

      val dna3 = "ACACACTA"
      val dna4 = "AGCACACA"
      val bestAlignment = ("A-CACACTA".toIndexedSeq, "AGCACAC-A".toIndexedSeq)

      val swAlignment = optimalAlignment[IndexedSeq, Char, DoubleMatrix, Int, Int](
        dna3, dna4, w, mismatchPenalty, gap)

      val space = SmithWatermanMetricSpace[IndexedSeq, Char, DoubleMatrix, Int, Int](w, mismatchPenalty)

      swAlignment must be equalTo bestAlignment
      space.distance(dna3, dna4) must be equalTo 12
    }
  }

}
