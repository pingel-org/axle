package axle.bio

import org.specs2.mutable._
import axle.jblas._
import org.jblas.DoubleMatrix
import spire.algebra.AdditiveMonoid
import spire.algebra.Ring
import spire.algebra.Module
import spire.algebra.Order

class AlignDNA extends Specification {

  "Needleman-Wunsch" should {
    "work" in {

      import NeedlemanWunsch.optimalAlignment
      import NeedlemanWunsch.Default.similarity
      import NeedlemanWunsch.Default.gap
      import NeedlemanWunsch.Default.gapPenalty

      implicit val laJblasDouble = {
        import spire.implicits.DoubleAlgebra
        linearAlgebraDoubleMatrix[Double]
      }

      import spire.implicits.CharAlgebra
      import spire.implicits.IntAlgebra
      import axle.algebra.modules.doubleIntModule
      implicit val amd: AdditiveMonoid[Double] = spire.implicits.DoubleAlgebra
      implicit val od: Order[Double] = spire.implicits.DoubleAlgebra

      val (a1, a2): (Stream[Char], Stream[Char]) =
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
