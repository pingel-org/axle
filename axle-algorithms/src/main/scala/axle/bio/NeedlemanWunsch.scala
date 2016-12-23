package axle.bio

import scala.Stream.cons
import scala.Stream.empty
import scala.Vector
import scala.reflect.ClassTag
import axle.orderToOrdering

import NeedlemanWunsch.computeF
import axle.algebra.Aggregatable
import axle.algebra.AggregatableK1
import axle.algebra.Finite
import axle.algebra.FiniteK1
import axle.algebra.FromStream
import axle.algebra.Functor
import axle.algebra.FunctorK1
import axle.algebra.Indexed
import axle.algebra.IndexedK1
import axle.algebra.LinearAlgebra
import axle.algebra.Zipper
import axle.algebra.ZipperK1
import axle.algebra.Σ
import axle.syntax.finite.finiteOps
import axle.syntax.functor.functorOps
import axle.syntax.indexed.indexedOps
import axle.syntax.linearalgebra.matrixOps
import spire.algebra.AdditiveMonoid
import spire.algebra.MetricSpace
import spire.algebra.Module
import spire.algebra.Ring
import spire.implicits.additiveGroupOps
import spire.implicits.additiveSemigroupOps
import spire.implicits.moduleOps
import spire.implicits.partialOrderOps
import cats.kernel.Order
import cats.kernel.Eq
import cats.implicits._

/**
 *
 * http://en.wikipedia.org/wiki/Needleman-Wunsch_algorithm
 *
 */

object NeedlemanWunsch {

  def alignmentScoreK1[C[_], N: Eq: ClassTag, I: Ring: Eq, M, V: AdditiveMonoid: Eq](
    a: C[N],
    b: C[N],
    gap: N,
    similarity: (N, N) => V,
    gapPenalty: V)(
      implicit indexed: IndexedK1[C, I, N],
      finite: FiniteK1[C, N, I],
      zipper: ZipperK1[C, N, N],
      functor: FunctorK1[C, (N, N), V],
      agg: AggregatableK1[C, V, V]): V =
    alignmentScore(a, b, gap, similarity, gapPenalty)

  /**
   * Computes the alignment score
   *
   * Arguments A and B must be of the same length
   *
   * alignmentScore("AGACTAGTTAC", "CGA---GACGT")
   *
   * ←
   */

  def alignmentScore[S, N: Eq: ClassTag, I: Ring: Eq, M, V: AdditiveMonoid: Eq, SS, G](
    A: S,
    B: S,
    gap: N,
    similarity: (N, N) => V,
    gapPenalty: V)(
      implicit indexed: Indexed[S, I, N],
      finite: Finite[S, I],
      zipper: Zipper[S, N, S, N, SS],
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

case class NeedlemanWunschMetricSpace[S, N: Eq, M, I: Ring: Order, V: AdditiveMonoid: Order](
  similarity: (N, N) => V,
  gapPenalty: V)(
    implicit la: LinearAlgebra[M, I, I, V],
    indexed: Indexed[S, I, N],
    finite: Finite[S, I],
    module: Module[V, I])
    extends MetricSpace[S, V] {

  def distance(s1: S, s2: S): V =
    computeF(s1, s2, similarity, gapPenalty).get(s1.size, s2.size)

}

object NeedlemanWunschMetricSpace {

  def common[U[_], N: Eq, M, I: Ring: Order, V: AdditiveMonoid: Order](
    similarity: (N, N) => V,
    gapPenalty: V)(
      implicit la: LinearAlgebra[M, I, I, V],
      indexed: Indexed[U[N], I, N],
      finite: Finite[U[N], I],
      module: Module[V, I]) =
    NeedlemanWunschMetricSpace(similarity, gapPenalty)
}
