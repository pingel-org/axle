package axle.ml

import axle.square
import math.sqrt

object FeatureNormalizer {

  import axle.matrix.JblasMatrixFactory._ // TODO: generalize
  type M[T] = JblasMatrix[T]

  trait FeatureNormalizer {

    def normalizedData(): M[Double]

    def normalize(featureList: Seq[Double]): M[Double]

    def denormalize(featureRow: M[Double]): Seq[Double]
  }

  class IdentityFeatureNormalizer(X: M[Double]) extends FeatureNormalizer {

    def normalizedData(): M[Double] = X

    def normalize(featureList: Seq[Double]): M[Double] =
      matrix(1, featureList.length, featureList.toArray)

    def denormalize(featureRow: M[Double]): Seq[Double] =
      (0 until featureRow.length).map(c => featureRow(0, c))
  }

  class LinearFeatureNormalizer(X: M[Double]) extends FeatureNormalizer {

    val colMins = X.columnMins
    val colRanges = X.columnMaxs - colMins
    val nd = X.subRowVector(colMins).divRowVector(colRanges)

    def normalizedData(): M[Double] = nd

    def normalize(features: Seq[Double]): M[Double] =
      matrix(1, features.length, features.toArray).subRowVector(colMins).divPointwise(colRanges)

    def denormalize(featureRow: M[Double]): Seq[Double] =
      (featureRow.mulPointwise(colRanges) + colMins).toList

  }

  class ZScoreFeatureNormalizer(X: M[Double]) extends FeatureNormalizer {

    lazy val μs = X.columnMeans
    lazy val σ2s = std(X)
    val nd = zscore(X)

    def normalizedData(): M[Double] = nd

    def normalize(features: Seq[Double]): M[Double] =
      (matrix(1, features.length, features.toArray) - μs).divPointwise(σ2s)

    def denormalize(featureRow: M[Double]): Seq[Double] =
      (σ2s.mulPointwise(featureRow) + μs).toList
  }

  class PCAFeatureNormalizer(X: M[Double], cutoff: Double) extends FeatureNormalizer {

    lazy val μs = X.columnMeans
    lazy val σ2s = std(X)
    val zd = zscore(X)

    val (u, s) = pca(zd)

    val k = numComponentsForCutoff(s, cutoff)

    val truncatedU = u.dup
    (k until u.columns).map(c => (0 until u.rows).map(r => truncatedU(r, c) = 0.0))

    def normalizedData(): M[Double] = zd ⨯ truncatedU

    def normalize(features: Seq[Double]): M[Double] =
      (matrix(1, features.length, features.toArray) - μs).divPointwise(σ2s)

    def denormalize(featureRow: M[Double]): Seq[Double] =
      (σ2s.mulPointwise(featureRow) + μs).toList
  }

}