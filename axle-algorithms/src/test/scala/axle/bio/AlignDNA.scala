package axle.bio

import org.specs2.mutable._
import axle.jblas.ConvertedJblasDoubleMatrix.jblasConvertedMatrix

class AlignDNA extends Specification {

  "Needleman-Wunsch" should {
    "work" in {

      NeedlemanWunsch.optimalAlignment("ATGCGGCC", "ATCGCCGG") must be equalTo ("ATGCGGCC--", "AT-C-GCCGG")
    }
  }

  "Smith-Waterman" should {
    "work" in {

      SmithWaterman.optimalAlignment("ACACACTA", "AGCACACA") must be equalTo ("A-CACACTA", "AGCACAC-A")
    }
  }

}
