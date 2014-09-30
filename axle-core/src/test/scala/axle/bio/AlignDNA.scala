package axle.bio

import org.specs2.mutable._
import axle.matrix.JblasMatrixModule

class AlignDNA extends Specification {

  "Needleman-Wunsch" should {
    "work" in {

      val nw = new NeedlemanWunsch with JblasMatrixModule

      nw.optimalAlignment("ATGCGGCC", "ATCGCCGG") must be equalTo ("ATGCGGCC--", "AT-C-GCCGG")
    }
  }

  "Smith-Waterman" should {
    "work" in {

      val sw = new SmithWaterman with JblasMatrixModule

      sw.optimalAlignment("ACACACTA", "AGCACACA") must be equalTo ("A-CACACTA", "AGCACAC-A")
    }
  }


}
