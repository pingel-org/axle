package axle.bio

import org.specs2.mutable._

class AlignDNA extends Specification {

  "Needleman-Wunsch" should {
    "work" in {

      import NeedlemanWunsch._

      optimalAlignment("ATGCGGCC", "ATCGCCGG") must be equalTo ("ATGCGGCC--", "AT-C-GCCGG")
    }
  }

  "Smith-Waterman" should {
    "work" in {

      import SmithWaterman._

      optimalAlignment("ACACACTA", "AGCACACA") must be equalTo ("A-CACACTA", "AGCACAC-A")
    }
  }


}