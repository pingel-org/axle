package axle.ml

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
      (0 until featureRow.length).map(r => featureRow(r, 0))
  }

  class LinearFeatureNormalizer(X: M[Double]) extends FeatureNormalizer {

    val colMins = X.columnMins
    val colRanges = X.columnMaxs - colMins
    val nd = (diag(colRanges).inv ⨯ X.subRowVector(colMins).t).t

    def normalizedData(): M[Double] = nd

    def normalize(featureList: Seq[Double]): M[Double] =
      matrix(1, featureList.length, featureList.toArray).subRowVector(colMins).divPointwise(colRanges)

    def denormalize(featureRow: M[Double]): Seq[Double] = (featureRow.mulPointwise(colRanges) + colMins).toList

  }

}