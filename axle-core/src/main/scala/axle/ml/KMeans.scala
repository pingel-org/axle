package axle.ml

import scala.Vector
import scala.collection.immutable.TreeMap

import cats.Functor
import cats.kernel.Eq
import cats.implicits._

import spire.algebra.MetricSpace
import spire.random.Generator

import axle.shuffle
import axle.algebra.LinearAlgebra
import axle.algebra.Finite
import axle.algebra.Indexed
import axle.syntax.finite._
import axle.syntax.indexed._
import axle.syntax.linearalgebra._

/**
 * KMeans
 *
 * @tparam T  type of the objects being classified
 *
 * @param data
 * @param N
 * @param featureExtractor
 * @param constructor
 * @param μ                K x N Matrix[Double], where each row is a centroid
 * @param scaledX          M x N
 * @param A                M x 1
 * @param distanceLog      K x iterations
 *
 * Try  distance (MetricSpace) distance.euclidean
 *
 */

case class KMeans[T: Eq, F[_], M](
  data:             F[T],
  N:                Int,
  featureExtractor: T => Seq[Double],
  normalizerMaker:  M => Normalize[M],
  K:                Int,
  iterations:       Int)(
  gen: Generator)(
  implicit
  space:   MetricSpace[M, Double],
  functor: Functor[F],
  val la:  LinearAlgebra[M, Int, Int, Double],
  index:   Indexed[F, Int],
  finite:  Finite[F, Int])
  extends Function1[T, Int] {

  val features = data.map(featureExtractor)

  val featureMatrix = la.matrix(data.size.toInt, N, (r: Int, c: Int) => features.at(r).apply(c))

  val normalizer = normalizerMaker(featureMatrix)

  val X = normalizer.normalizedData

  val μads = clusterLA(X, space, K, iterations)(gen)

  val (μ, a, d) = μads.last

  val assignmentLog = μads.map(_._2)
  val distanceLog = μads.map(_._3)

  def centroid(i: Int): Seq[Double] = normalizer.unapply(μ.row(i))

  def classes: Range = 0 until K

  def apply(observation: T): Int = {
    val (i, d) = centroidIndexAndDistanceClosestTo(space, μ, normalizer(featureExtractor(observation)))
    i
  }

  /**
   * centroidIndexAndDistanceClosestTo
   *
   * @param μ
   * @param x
   */

  def centroidIndexAndDistanceClosestTo(
    space: MetricSpace[M, Double],
    μ:     M,
    x:     M): (Int, Double) =
    (0 until μ.rows).map(r => (r, space.distance(μ.row(r), x))).minBy(_._2)

  /**
   * assignmentsAndDistances
   *
   * @param X
   * @param μ
   *
   * Returns:
   * N x 1 matrix: indexes of centroids closest to xi
   * N x 1 matrix: distances to those centroids
   */

  def assignmentsAndDistances(
    space: MetricSpace[M, Double],
    X:     M,
    μ:     M): (M, M) = {
    val AD = (0 until X.rows).map(r => {
      val xi = X.row(r)
      val (a, d) = centroidIndexAndDistanceClosestTo(space, μ, xi)
      Vector(a.toDouble, d)
    }).transpose
    (la.fromColumnMajorArray(X.rows, 1, AD(0).toArray), la.fromColumnMajorArray(X.rows, 1, AD(1).toArray))
  }

  /**
   * clusterLA
   *
   * @param  X (normalized feature matrix)
   * @param  distance
   * @param  K
   * @param  iterations
   *
   */

  def clusterLA(
    X:          M,
    space:      MetricSpace[M, Double],
    K:          Int,
    iterations: Int)(gen: Generator): Seq[(M, M, M)] = {

    assert(K < X.rows)

    val selection = shuffle((0 until X.rows).toList)(gen).take(K)
    val μ0 = X.slice(selection, 0 until X.columns)
    val a0 = la.zeros(X.rows, 1)
    val d0 = la.zeros(X.rows, 1)

    (0 until iterations).scanLeft((μ0, a0, d0))((μad: (M, M, M), i: Int) => {
      val (a, d) = assignmentsAndDistances(space, X, μad._1)
      val (μ, unassignedClusterIds) = centroids(X, K, a)
      // val replacements = scaledX(shuffle(0 until scaledX.rows).take(unassignedClusterIds.length), 0 until scaledX.columns)
      (μ, a, d)
    }).tail
  }

  /**
   * centroids
   *
   * @param X            M x N scaled feature matrix
   * @param K            number of centroids
   * @param assignments: M x 1 column vector of Ints which e
   *
   */

  def centroids(X: M, K: Int, assignments: M): (M, Seq[Int]) = {

    import cats.implicits._ // for ===

    val A = la.matrix(X.rows, K, (r: Int, c: Int) => if (c === assignments.get(r, 0).toInt) 1d else 0d)
    val distances = la.multiplicative.times(A.t, X) // K x N
    val counts = A.columnSums.t // K x 1
    val unassignedClusterIds = (0 until K).filter(counts.get(_, 0) === 0d)

    (distances.divColumnVector(counts), unassignedClusterIds)
  }

  def distanceTreeMap(centroidId: Int): TreeMap[Int, Double] = new TreeMap[Int, Double]() ++
    distanceLog.zipWithIndex.map({ case (dl, i) => i -> dl.get(centroidId, 0) }).toMap

  def distanceLogSeries: List[(Int, TreeMap[Int, Double])] =
    (0 until K).map(i => (i, distanceTreeMap(i))).toList

}

