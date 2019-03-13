package axle.bio

import org.jblas.DoubleMatrix
import org.scalatest._
import org.scalacheck.Gen
import org.scalacheck.Arbitrary
import org.scalacheck.Properties
import org.scalacheck.Prop.forAll

import cats.implicits._

import spire.algebra._

import axle.algebra.functorIndexedSeq

object SharedNeedlemanWunsch {

  import NeedlemanWunschDefaults._

  implicit val ringInt: Ring[Int] = spire.implicits.IntAlgebra
  implicit val dim: Module[Double, Int] = axle.algebra.modules.doubleIntModule

  implicit val laJblasDouble = {
    implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
    implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra
    axle.jblas.linearAlgebraDoubleMatrix[Double]
  }

  implicit val space = NeedlemanWunschSimilaritySpace[IndexedSeq, Char, DoubleMatrix, Int, Double](
    similarity, gapPenalty)

}

class NeedlemanWunschSpec extends FunSuite with Matchers {

  import NeedlemanWunsch.alignmentScore
  import NeedlemanWunsch.optimalAlignment
  import NeedlemanWunschDefaults._
  import SharedNeedlemanWunsch._

  test("Needleman-Wunsch DNA alignment") {

    val dna1 = "ATGCGGCC"
    val dna2 = "ATCGCCGG"
  
    val nwAlignment =
      optimalAlignment[IndexedSeq, Char, DoubleMatrix, Int, Double](
        dna1, dna2, similarity, gap, gapPenalty)
  
    val score = alignmentScore(
      nwAlignment._1,
      nwAlignment._2,
      gap,
      similarity,
      gapPenalty)
  
    nwAlignment should be(("ATGCGGCC--".toIndexedSeq, "AT-C-GCCGG".toIndexedSeq))
    score should be(32d)
    space.similarity(dna1, dna2) should be(score)
  }

}

class NeedlemanWunschLawfulSpec extends Properties("Needleman-Wunsch") {

  import SharedNeedlemanWunsch._

  implicit val genChar: Gen[Char] = Gen.oneOf('A', 'T', 'G', 'C')
  implicit val arbChar: Arbitrary[Char] = Arbitrary(genChar)

  property("most similar to itself") = forAll { (a: IndexedSeq[Char], b: IndexedSeq[Char]) =>
    (a == b) || (space.similarity(a, a) > space.similarity(a, b))
  }

  property("symmetry") = forAll { (a: IndexedSeq[Char], b: IndexedSeq[Char]) =>
    space.similarity(a, b) == space.similarity(b, a)
  }
}

object SharedSmithWaterman {
 
  import SmithWatermanDefaults._

  import spire.algebra._

  implicit val ringInt: Ring[Int] = spire.implicits.IntAlgebra
  implicit val nrootInt: NRoot[Int] = spire.implicits.IntAlgebra

  implicit val laJblasInt = axle.jblas.linearAlgebraDoubleMatrix[Int]

  implicit val space = SmithWatermanSimilaritySpace[IndexedSeq, Char, DoubleMatrix, Int, Int](w, mismatchPenalty)

}

class SmithWatermanSpec extends FunSuite with Matchers {

  import SharedSmithWaterman._
  import SmithWatermanDefaults._
  import SmithWaterman.optimalAlignment

  test("Smith-Waterman") {

    val dna3 = "ACACACTA"
    val dna4 = "AGCACACA"
    val bestAlignment = ("A-CACACTA".toIndexedSeq, "AGCACAC-A".toIndexedSeq)

    val swAlignment = optimalAlignment[IndexedSeq, Char, DoubleMatrix, Int, Int](
        dna3, dna4, w, mismatchPenalty, gap)

    swAlignment should be(bestAlignment)
    space.similarity(dna3, dna4) should be(12)
  }
}

class SmithWatermanLawfulSpec extends Properties("Smith-Waterman") {

  import SharedSmithWaterman._

  implicit val genChar: Gen[Char] = Gen.oneOf('A', 'T', 'G', 'C')
  implicit val arbChar: Arbitrary[Char] = Arbitrary(genChar)
  
  property("most similar to itself") = forAll { (a: IndexedSeq[Char], b: IndexedSeq[Char]) =>
    (a == b) || (space.similarity(a, a) > space.similarity(a, b))
  }

  property("symmetry") = forAll { (a: IndexedSeq[Char], b: IndexedSeq[Char]) =>
    space.similarity(a, b) == space.similarity(b, a)
  }
}
