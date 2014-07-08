package axle.ml

import scala.Vector
import scala.collection.immutable.TreeMap
import scala.util.Random.shuffle

import FeatureNormalizerModule.PCAFeatureNormalizer
import axle.matrix.JblasMatrixModule.Matrix
import axle.matrix.JblasMatrixModule.convertDouble
import axle.matrix.JblasMatrixModule.convertInt
import axle.matrix.JblasMatrixModule.matrix
import axle.matrix.JblasMatrixModule.zeros
import spire.algebra.Eq
import spire.algebra.MetricSpace
import spire.implicits.DoubleAlgebra
import spire.implicits.IntAlgebra
import spire.implicits.eqOps

/**
 * KMeans
 *
 */

object KMeansModule extends KMeansModule

trait KMeansModule {

  /**
   * cluster[T]
   *
   * @tparam T  type of the objects being classified
   *
   * @param data
   * @param N
   * @param featureExtractor
   * @param constructor
   *
   */

  def classifier[T: Eq](
    data: Seq[T],
    N: Int,
    featureExtractor: T => Seq[Double],
    constructor: Seq[Double] => T,
    K: Int,
    iterations: Int)(implicit space: MetricSpace[Matrix[Double], Double]): KMeansClassifier[T] =
    KMeansClassifier(data, N, featureExtractor, constructor, K, iterations)

  // TODO: default distance = distance.euclidean

  /**
   * KMeansClassifier[T]
   *
   * @tparam T       type of the objects being classified
   *
   * @param N                number of features
   * @param featureExtractor creates a list of features (Doubles) of length N given a T
   * @param constructor      creates a T from list of arguments of length N
   * @param μ                K x N Matrix[Double], where each row is a centroid
   * @param scaledX          M x N
   * @param A                M x 1
   * @param distanceLog      K x iterations
   */

  case class KMeansClassifier[T: Eq](
    data: Seq[T],
    N: Int,
    featureExtractor: T => Seq[Double],
    constructor: Seq[Double] => T,
    K: Int,
    iterations: Int)(implicit space: MetricSpace[Matrix[Double], Double]) extends Classifier[T, Int] {

    val features = matrix(N, data.length, data.flatMap(featureExtractor).toArray).t

    val normalizer = new PCAFeatureNormalizer(features, 0.98)
    //val normalizer = new ZScoreFeatureNormalizer(features)
    val X = normalizer.normalizedData
    val μads = clusterLA(X, space, K, iterations)

    val (μ, a, d) = μads.last

    val assignmentLog = μads.map(_._2)
    val distanceLog = μads.map(_._3)

    val exemplars = (0 until K).map(i => constructor(normalizer.denormalize(μ.row(i)))).toList

    def exemplar(i: Int): T = exemplars(i)

    def classes(): Range = 0 until K

    def apply(observation: T): Int = {
      val (i, d) = centroidIndexAndDistanceClosestTo(space, μ, normalizer.normalize(featureExtractor(observation)))
      i
    }

    /**
     * centroidIndexAndDistanceClosestTo
     *
     * @param μ
     * @param x
     */

    def centroidIndexAndDistanceClosestTo(
      space: MetricSpace[Matrix[Double], Double],
      μ: Matrix[Double],
      x: Matrix[Double]): (Int, Double) =
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
      space: MetricSpace[Matrix[Double], Double],
      X: Matrix[Double],
      μ: Matrix[Double]): (Matrix[Int], Matrix[Double]) = {
      val AD = (0 until X.rows).map(r => {
        val xi = X.row(r)
        val (a, d) = centroidIndexAndDistanceClosestTo(space, μ, xi)
        Vector(a, d)
      }).transpose
      // TODO: remove the map(_.toInt)
      (matrix(X.rows, 1, AD(0).map(_.toInt).toArray), matrix(X.rows, 1, AD(1).toArray))
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
      X: Matrix[Double],
      space: MetricSpace[Matrix[Double], Double],
      K: Int,
      iterations: Int): Seq[(Matrix[Double], Matrix[Int], Matrix[Double])] = {
      assert(K < X.rows)
      val μ0 = X(shuffle((0 until X.rows).toList).take(K), 0 until X.columns)
      val a0 = zeros[Int](X.rows, 1)
      val d0 = zeros[Double](X.rows, 1)
      (0 until iterations).scanLeft((μ0, a0, d0))((μad: (Matrix[Double], Matrix[Int], Matrix[Double]), i: Int) => {
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

    def centroids(X: Matrix[Double], K: Int, assignments: Matrix[Int]): (Matrix[Double], Seq[Int]) = {
      val A = matrix(X.rows, K, (r: Int, c: Int) => if (c === assignments(r, 0)) 1d else 0d)
      val distances = A.t ⨯ X // K x N
      val counts = A.columnSums.t // K x 1
      val unassignedClusterIds = (0 until K).filter(counts(_, 0) === 0d)
      (distances.divColumnVector(counts), unassignedClusterIds)
    }

    def distanceTreeMap(centroidId: Int): TreeMap[Int, Double] = new TreeMap[Int, Double]() ++
      distanceLog.zipWithIndex.map({ case (dl, i) => i -> dl(centroidId, 0) }).toMap

    def distanceLogSeries(): List[(String, TreeMap[Int, Double])] =
      (0 until K).map(i => ("centroid " + i, distanceTreeMap(i))).toList

  }

}
