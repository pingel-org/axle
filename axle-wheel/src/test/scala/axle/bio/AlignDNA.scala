package axle.bio

import org.jblas.DoubleMatrix
import org.scalatest._
import org.typelevel.discipline.scalatest.Discipline

import cats.implicits._

import spire.algebra._
import spire.laws._

import axle.algebra.functorIndexedSeq
import axle.algebra.eqIndexedSeq

class NeedlemanWunschSpec extends FunSuite with Matchers with Discipline {

  import NeedlemanWunsch.alignmentScore
  import NeedlemanWunsch.optimalAlignment
  import NeedlemanWunschDefaults._

  implicit val ringInt: Ring[Int] = spire.implicits.IntAlgebra
  implicit val dim: Module[Double, Int] = axle.algebra.modules.doubleIntModule

  implicit val laJblasDouble = {
    implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
    implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra
    axle.jblas.linearAlgebraDoubleMatrix[Double]
  }

  implicit val space = NeedlemanWunschMetricSpace[IndexedSeq, Char, DoubleMatrix, Int, Double](
    similarity, gapPenalty)

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
    space.distance(dna1, dna2) should be(score)
  }

  checkAll(
    "NeedlemanWunsch as MetricSpace[IndexedSeq[Char], Double]",
    VectorSpaceLaws[IndexedSeq[Char], Double].metricSpace)

}

class SmithWatermanSpec extends FunSuite with Matchers with Discipline {

  import SmithWatermanDefaults._
  import SmithWaterman.optimalAlignment

  import spire.algebra._

  implicit val ringInt: Ring[Int] = spire.implicits.IntAlgebra
  implicit val nrootInt: NRoot[Int] = spire.implicits.IntAlgebra

  implicit val laJblasInt = axle.jblas.linearAlgebraDoubleMatrix[Int]

  implicit val space = SmithWatermanMetricSpace[IndexedSeq, Char, DoubleMatrix, Int, Int](w, mismatchPenalty)

  test("Smith-Waterman") {

    val dna3 = "ACACACTA"
    val dna4 = "AGCACACA"
    val bestAlignment = ("A-CACACTA".toIndexedSeq, "AGCACAC-A".toIndexedSeq)

    val swAlignment = optimalAlignment[IndexedSeq, Char, DoubleMatrix, Int, Int](
        dna3, dna4, w, mismatchPenalty, gap)

    swAlignment should be(bestAlignment)
    space.distance(dna3, dna4) should be(12)
  }

  checkAll(
    "Smith-Waterman as MetricSpace[IndexedSeq[Char], Int]",
    VectorSpaceLaws[IndexedSeq[Char], Int].metricSpace)

}
