package axle.bio

import scala.Vector
import scala.Stream.cons
import scala.Stream.empty

import NeedlemanWunsch.computeF
import axle.algebra.Σ
import axle.algebra.Aggregatable
import axle.algebra.Finite
import axle.algebra.FromStream
import axle.algebra.Functor
import axle.algebra.Indexed
import axle.algebra.LinearAlgebra
import axle.algebra.Zipper
import axle.syntax.finite.finiteOps
import axle.syntax.functor.functorOps
import axle.syntax.indexed.indexedOps
import axle.syntax.linearalgebra.matrixOps
import spire.algebra.AdditiveMonoid
import spire.algebra.Eq
import spire.algebra.MetricSpace
import spire.algebra.Module
import spire.algebra.Order
import spire.algebra.Ring
import spire.compat.ordering
import spire.implicits.additiveGroupOps
import spire.implicits.additiveSemigroupOps
import spire.implicits.eqOps
import spire.implicits.moduleOps
import spire.implicits.partialOrderOps

/**
 *
 * http://en.wikipedia.org/wiki/Needleman-Wunsch_algorithm
 *
 */

object NeedlemanWunsch {

  object Default {

    // Default evidence for optimalAlignment[IndexedSeq, Char, DoubleMatrix, Int, Double]

    implicit val charEq: Eq[Char] = spire.implicits.CharAlgebra
    implicit val intRing: Ring[Int] = spire.implicits.IntAlgebra
    implicit val orderRing: Order[Int] = spire.implicits.IntAlgebra
    implicit val dim: Module[Double, Int] = axle.algebra.modules.doubleIntModule
    implicit val amd: AdditiveMonoid[Double] = spire.implicits.DoubleAlgebra
    implicit val od: Order[Double] = spire.implicits.DoubleAlgebra

    /**
     * similarity function for nucleotides
     *
     * S(a, b) === S(b, a)
     *
     */

    def similarity(x: Char, y: Char): Double =
      (x, y) match {
        case ('A', 'A') => 10
        case ('A', 'G') => -1
        case ('A', 'C') => -3
        case ('A', 'T') => -4
        case ('G', 'A') => -1
        case ('G', 'G') => 7
        case ('G', 'C') => -5
        case ('G', 'T') => -3
        case ('C', 'A') => -3
        case ('C', 'G') => -5
        case ('C', 'C') => 9
        case ('C', 'T') => 0
        case ('T', 'A') => -4
        case ('T', 'G') => -3
        case ('T', 'C') => 0
        case ('T', 'T') => 8
      }

    val gap = '-'

    val gapPenalty = -5d

  }

  /**
   * Computes the alignment score
   *
   * Arguments A and B must be of the same length
   *
   * alignmentScore("AGACTAGTTAC", "CGA---GACGT")
   *
   * ←
   */

  def alignmentScore[S, N: Eq, I: Ring: Eq, M, V: AdditiveMonoid: Eq, SS, G](
    A: S,
    B: S,
    gap: N,
    similarity: (N, N) => V,
    gapPenalty: V)(
      implicit indexed: Indexed[S, I, V],
      finite: Finite[S, I],
      zipper: Zipper[S, V, SS],
      functor: Functor[SS, (N, N), V, G],
      agg: Aggregatable[G, V, V]): V = {

    assert(A.size === B.size)

    val zipped = zipper.zip(A, B)

    val scores: G =
      zipped.map({
        case (a: N, b: N) =>
          if (a === gap || b === gap) { gapPenalty } else { similarity(a, b) }
      })

    Σ[V, G](scores)
  }

  /**
   *
   * Computes the "F" matrix for two nucleotide sequences, A and B
   *
   */

  def computeF[I: Ring, S, N, M, V: AdditiveMonoid: Order](
    A: S,
    B: S,
    similarity: (N, N) => V,
    gapPenalty: V)(
      implicit la: LinearAlgebra[M, I, I, V],
      indexed: Indexed[S, I, N],
      finite: Finite[S, I],
      module: Module[V, I]): M = {

    val one = Ring[I].one

    la.matrix(
      A.size + one,
      B.size + one,
      implicitly[AdditiveMonoid[V]].zero,
      (i: I) => i *: gapPenalty,
      (j: I) => j *: gapPenalty,
      (i: I, j: I, aboveleft: V, left: V, above: V) => {
        Vector(
          aboveleft + similarity(A.at(i - one), B.at(j - one)),
          above + gapPenalty,
          left + gapPenalty).max
      })

  }

  def alignStep[S, N: Eq, M, I: Ring: Order, V: AdditiveMonoid: Eq](
    i: I,
    j: I,
    A: S,
    B: S,
    F: M,
    similarity: (N, N) => V,
    gap: N,
    gapPenalty: V)(
      implicit la: LinearAlgebra[M, I, I, V],
      indexed: Indexed[S, I, N]): (N, N, I, I) = {

    val one = Ring[I].one
    val zero = Ring[I].zero

    if ((i > zero) && (j > zero) && (F.get(i, j) === (F.get(i - one, j - one) + similarity(A.at(i - one), B.at(j - one))))) {
      (A.at(i - one), B.at(j - one), i - one, j - one)
    } else if (i > zero && F.get(i, j) === (F.get(i - one, j) + gapPenalty)) {
      (A.at(i - one), gap, i - one, j)
    } else {
      assert(j > zero && F.get(i, j) === F.get(i, j - one) + gapPenalty)
      (gap, B.at(j - one), i, j - one)
    }
  }

  def _optimalAlignment[S, N: Eq, M, I: Ring: Order, V: AdditiveMonoid: Eq](
    i: I,
    j: I,
    A: S,
    B: S,
    similarity: (N, N) => V,
    gap: N,
    gapPenalty: V,
    F: M)(
      implicit la: LinearAlgebra[M, I, I, V],
      indexed: Indexed[S, I, N]): Stream[(N, N)] = {

    val zero = Ring[I].zero

    if ((i > zero) || (j > zero)) {
      val (preA, preB, newI, newJ) = alignStep(i, j, A, B, F, similarity, gap, gapPenalty)
      cons((preA, preB), _optimalAlignment(newI, newJ, A, B, similarity, gap, gapPenalty, F))
    } else {
      empty
    }
  }

  def optimalAlignment[S, N: Eq, M, I: Ring: Order, V: AdditiveMonoid: Order: Eq](
    A: S,
    B: S,
    similarity: (N, N) => V,
    gap: N,
    gapPenalty: V)(
      implicit la: LinearAlgebra[M, I, I, V],
      indexed: Indexed[S, I, N],
      finite: Finite[S, I],
      fs: FromStream[S, N],
      module: Module[V, I]): (S, S) = {

    val F = computeF(A, B, similarity, gapPenalty)

    val (alignA, alignB) = _optimalAlignment(
      A.size,
      B.size,
      A,
      B,
      similarity,
      gap,
      gapPenalty,
      F).reverse.unzip

    (fs.fromStream(alignA), fs.fromStream(alignB))
  }

}

case class NeedlemanWunschMetricSpace[S, C: Eq, M, I: Ring: Order, V: AdditiveMonoid: Order](
  similarity: (C, C) => V,
  gapPenalty: V)(
    implicit la: LinearAlgebra[M, I, I, V],
    indexed: Indexed[S, I, C],
    finite: Finite[S, I],
    module: Module[V, I])
    extends MetricSpace[S, V] {

  def distance(s1: S, s2: S): V =
    computeF(s1, s2, similarity, gapPenalty).get(s1.size, s2.size)

}
