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
    val nd = (diag(colRanges).inv ⨯ X.subRowVector(colMins).t).t // SLOW

    def normalizedData(): M[Double] = nd

    def normalize(features: Seq[Double]): M[Double] =
      matrix(1, features.length, features.toArray).subRowVector(colMins).divPointwise(colRanges)

    def denormalize(featureRow: M[Double]): Seq[Double] = (featureRow.mulPointwise(colRanges) + colMins).toList

  }

  class ZScoreFeatureNormalizer(X: M[Double]) extends FeatureNormalizer {

    val μs = (X.columnSums() / X.rows()).toList

    val μRow = matrix(1, μs.length, μs.toArray)

    val σ2s = μs.zipWithIndex.map({
      case (μ, c) => sqrt((0 until X.rows).map(r => square(X(r, c) - μ)).sum) // 1.0 when this is 0.0 ?
    })

    val σ2Row = matrix(1, σ2s.length, σ2s.toArray)

    val nd = (diag(σ2Row).inv ⨯ X.subRowVector(μRow).t).t // SLOW

    def normalizedData(): M[Double] = nd

    def normalize(features: Seq[Double]): M[Double] =
      matrix(1, features.length, features.zip(μs.zip(σ2s)).map({
        case (f, (μ, σ2)) => (f - μ) / σ2
      }).toArray)

    def denormalize(featureRow: M[Double]): Seq[Double] =
      μs.zip(σ2s).zipWithIndex.map({ case ((μ, σ2), c) => (σ2 * featureRow(0, c)) + μ })

  }

}