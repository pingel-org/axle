package axle.ml

import scala.Vector
import scala.collection.immutable.TreeMap
import scala.util.Random.shuffle
import scala.reflect.ClassTag

import axle.algebra.Matrix
import axle.algebra.Aggregatable
import axle.algebra.Finite
import axle.algebra.Functor
import axle.algebra.Indexed
import axle.algebra.Monad
import axle.syntax.matrix._

import spire.algebra.Eq
import spire.algebra.MetricSpace
import spire.implicits.DoubleAlgebra
import spire.implicits.IntAlgebra
import spire.implicits.eqOps

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
 */

case class KMeans[T: Eq: ClassTag, F[_]: Aggregatable: Functor: Finite: Indexed, M[_]](
  data: F[T],
  N: Int,
  featureExtractor: T => Seq[Double],
  normalizerMaker: M[Double] => Normalize[M],
  constructor: Seq[Double] => T,
  K: Int,
  iterations: Int)(implicit space: MetricSpace[M[Double], Double], ev: Matrix[M])
  extends Classifier[T, Int] {

  // TODO: default distance = distance.euclidean

  val finite = implicitly[Finite[F]]
  val functor = implicitly[Functor[F]]
  val indexed = implicitly[Indexed[F]]

  // TODO: This is not at all what we should be doing when F is a large RDD
  val features: F[Seq[Double]] = functor.map(data)(featureExtractor)
  val featureMatrix = ev.matrix[Double](finite.size(data).toInt, N, (r: Int, c: Int) => indexed.at(features)(r).apply(c))

  val normalizer = normalizerMaker(featureMatrix)

  val X = normalizer.normalizedData
  val μads = clusterLA(X, space, K, iterations)

  val (μ, a, d) = μads.last

  val assignmentLog = μads.map(_._2)
  val distanceLog = μads.map(_._3)

  val exemplars = (0 until K).map(i => constructor(normalizer.unapply(μ.row(i)))).toList

  def exemplar(i: Int): T = exemplars(i)

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
    space: MetricSpace[M[Double], Double],
    μ: M[Double],
    x: M[Double]): (Int, Double) =
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
    space: MetricSpace[M[Double], Double],
    X: M[Double],
    μ: M[Double]): (M[Int], M[Double]) = {
    val AD = (0 until X.rows).map(r => {
      val xi = X.row(r)
      val (a, d) = centroidIndexAndDistanceClosestTo(space, μ, xi)
      Vector(a, d)
    }).transpose
    // TODO: remove the map(_.toInt)
    (ev.matrix(X.rows, 1, AD(0).map(_.toInt).toArray), ev.matrix(X.rows, 1, AD(1).toArray))
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
    X: M[Double],
    space: MetricSpace[M[Double], Double],
    K: Int,
    iterations: Int): Seq[(M[Double], M[Int], M[Double])] = {

    assert(K < X.rows)

    val μ0 = X.slice(shuffle((0 until X.rows).toList).take(K), 0 until X.columns)
    val a0 = ev.zeros[Int](X.rows, 1)
    val d0 = ev.zeros[Double](X.rows, 1)

    (0 until iterations).scanLeft((μ0, a0, d0))((μad: (M[Double], M[Int], M[Double]), i: Int) => {
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

  def centroids(X: M[Double], K: Int, assignments: M[Int]): (M[Double], Seq[Int]) = {

    val A = ev.matrix(X.rows, K, (r: Int, c: Int) => if (c === assignments.get(r, 0)) 1d else 0d)
    val distances = A.t ⨯ X // K x N
    val counts = A.columnSums.t // K x 1
    val unassignedClusterIds = (0 until K).filter(counts.get(_, 0) === 0d)

    (distances.divColumnVector(counts), unassignedClusterIds)
  }

  def distanceTreeMap(centroidId: Int): TreeMap[Int, Double] = new TreeMap[Int, Double]() ++
    distanceLog.zipWithIndex.map({ case (dl, i) => i -> dl.get(centroidId, 0) }).toMap

  def distanceLogSeries: List[(String, TreeMap[Int, Double])] =
    (0 until K).map(i => ("centroid " + i, distanceTreeMap(i))).toList

}
