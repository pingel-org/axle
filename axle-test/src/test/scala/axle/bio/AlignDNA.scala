package axle.bio

import org.specs2.mutable._
import axle.jblas._

class AlignDNA extends Specification {

  "Needleman-Wunsch" should {
    "work" in {

      import spire.implicits.DoubleAlgebra
      implicit val laJblasDouble = linearAlgebraDoubleMatrix[Double]

      NeedlemanWunsch.optimalAlignment("ATGCGGCC", "ATCGCCGG") must be equalTo ("ATGCGGCC--", "AT-C-GCCGG")
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
