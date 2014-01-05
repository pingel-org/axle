package axle.ml

import math.sqrt

import axle.matrix._

object FeatureNormalizerModule extends FeatureNormalizerModule

trait FeatureNormalizerModule {

  // DistanceFunctionModule
  import axle.matrix.JblasMatrixModule._

  trait FeatureNormalizer {

    def normalizedData: Matrix[Double]

    def normalize(featureList: Seq[Double]): Matrix[Double]

    def denormalize(featureRow: Matrix[Double]): Seq[Double]

    def random(): Matrix[Double]
  }

  class IdentityFeatureNormalizer(X: Matrix[Double]) extends FeatureNormalizer {

    def normalizedData: Matrix[Double] = X

    def normalize(featureList: Seq[Double]): Matrix[Double] =
      matrix(1, featureList.length, featureList.toArray)

    def denormalize(featureRow: Matrix[Double]): Seq[Double] =
      featureRow.toList

    def random(): Matrix[Double] = matrix(1, X.columns, (0 until X.columns).map(i => math.random).toArray)
  }

  class LinearFeatureNormalizer(X: Matrix[Double]) extends FeatureNormalizer {

    val colMins = X.columnMins
    val colRanges = X.columnMaxs - colMins
    val nd = X.subRowVector(colMins).divRowVector(colRanges)

    def normalizedData: Matrix[Double] = nd

    def normalize(features: Seq[Double]): Matrix[Double] =
      matrix(1, features.length, features.toArray).subRowVector(colMins).divPointwise(colRanges)

    def denormalize(featureRow: Matrix[Double]): Seq[Double] =
      (featureRow.mulPointwise(colRanges) + colMins).toList

    def random(): Matrix[Double] = matrix(1, X.columns, (0 until X.columns).map(i => math.random).toArray).mulPointwise(colRanges) + colMins
  }

  class ZScoreFeatureNormalizer(X: Matrix[Double]) extends FeatureNormalizer {

    lazy val μs = X.columnMeans
    lazy val σ2s = std(X)
    val nd = zscore(X)

    def normalizedData: Matrix[Double] = nd

    def normalize(features: Seq[Double]): Matrix[Double] =
      (matrix(1, features.length, features.toArray) - μs).divPointwise(σ2s)

    def denormalize(featureRow: Matrix[Double]): Seq[Double] =
      (featureRow.mulPointwise(σ2s) + μs).toList

    def random(): Matrix[Double] = matrix(1, X.columns, (0 until X.columns).map(i => util.Random.nextGaussian).toArray)
  }

  class PCAFeatureNormalizer(X: Matrix[Double], cutoff: Double) extends FeatureNormalizer {

    lazy val μs = X.columnMeans
    lazy val σ2s = std(X)
    val zd = zscore(X)
    val (u, s) = pca(zd)
    val k = numComponentsForCutoff(s, cutoff)
    val Uk = u(0 until u.rows, 0 until k)

    def normalizedData: Matrix[Double] = zd ⨯ Uk

    def normalize(features: Seq[Double]): Matrix[Double] =
      (matrix(1, features.length, features.toArray) - μs).divPointwise(σ2s) ⨯ Uk

    def denormalize(featureRow: Matrix[Double]): Seq[Double] =
      ((featureRow ⨯ Uk.t).mulPointwise(σ2s) + μs).toList

    def random(): Matrix[Double] = matrix(1, X.columns, (0 until X.columns).map(i => util.Random.nextGaussian).toArray) ⨯ Uk

    // (truncatedSigmas.mulPointwise(featureRow) + truncatedMeans).toList
    // val truncatedSigmas = σ2s ⨯ Uk
    // val truncatedMeans = μs ⨯ Uk
    // ns = (fs - μs) .* σ2s ⨯ Uk
    // (ns ⨯ Uk') ./ σ2s + μs  = fs
  }

}
