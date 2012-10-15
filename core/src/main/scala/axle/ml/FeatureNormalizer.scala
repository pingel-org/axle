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

    val μs = X.columnMeans
    val σ2s = std(X)
    val nd = zscore(X)

    def normalizedData(): M[Double] = nd

    def normalize(features: Seq[Double]): M[Double] =
      (matrix(1, features.length, features.toArray) - μs).divPointwise(σ2s)

    def denormalize(featureRow: M[Double]): Seq[Double] = (σ2s.mulPointwise(featureRow) + μs).toList

  }

  class PCAFeatureNormalizer(X: M[Double], cutoff: Double) extends FeatureNormalizer {

    def truncateEigenValues(s: M[Double], cutoff: Double) = {
      val eigenValuesSquared = s.toList.map(square(_))
      val eigenTotal = eigenValuesSquared.sum
      val numComponents = eigenValuesSquared.map(_ / eigenTotal).scan(0.0)(_ + _).indexWhere(cutoff<)
      matrix(s.rows, 1, (0 until s.rows).map(r => if (r < numComponents) { s(r, 0) } else { 0.0 }).toArray)
    }

    val subNormalizer = new ZScoreFeatureNormalizer(X)

    val zd = subNormalizer.normalizedData

    val (u, s, v) = zd.fullSVD // Note: zd == u ⨯ diag(s.t) ⨯ v.t

    val nd = u ⨯ diag(truncateEigenValues(s, cutoff).t) ⨯ v.t

    def normalizedData(): M[Double] = nd

    def normalize(features: Seq[Double]): M[Double] = subNormalizer.normalize(features)

    def denormalize(featureRow: M[Double]): Seq[Double] = subNormalizer.denormalize(featureRow)

  }

  // https://mailman.cae.wisc.edu/pipermail/help-octave/2004-May/012772.html
  def pca3(X: M[Double]): (M[Double], M[Double], M[Double]) = {
    val (u, d, pc) = cov(X).fullSVD
    val z = centerColumns(X) ⨯ pc
    val w = diag(d.t)
    val Tsq = sumsq(zscore(z))
    (z, w, Tsq)
  }

}