package axle.ml

import axle.matrix.MatrixModule

trait FeatureNormalizerModule extends MatrixModule {

  trait Normalize extends (Seq[Double] => Matrix[Double]) {

    def normalizedData: Matrix[Double]

    def apply(featureList: Seq[Double]): Matrix[Double]

    def unapply(featureRow: Matrix[Double]): Seq[Double]

    def random(): Matrix[Double]
  }

  trait FeatureNormalizer {

    def normalizer(X: Matrix[Double]): Normalize
  }

  class IdentityFeatureNormalizer extends FeatureNormalizer {

    def normalizer(X: Matrix[Double]): Normalize = new Normalize {

      def normalizedData: Matrix[Double] = X

      def apply(featureList: Seq[Double]): Matrix[Double] =
        matrix(1, featureList.length, featureList.toArray)

      def unapply(featureRow: Matrix[Double]): Seq[Double] =
        featureRow.toList

      def random(): Matrix[Double] = matrix(1, X.columns, (0 until X.columns).map(i => math.random).toArray)
    }
  }

  class LinearFeatureNormalizer extends FeatureNormalizer {

    def normalizer(X: Matrix[Double]): Normalize = new Normalize {

      val colMins = X.columnMins
      val colRanges = X.columnMaxs - colMins
      val nd = X.subRowVector(colMins).divRowVector(colRanges)

      def normalizedData: Matrix[Double] = nd

      def apply(features: Seq[Double]): Matrix[Double] =
        matrix(1, features.length, features.toArray).subRowVector(colMins).divPointwise(colRanges)

      def unapply(featureRow: Matrix[Double]): Seq[Double] =
        (featureRow.mulPointwise(colRanges) + colMins).toList

      def random(): Matrix[Double] =
        matrix(1, X.columns, (0 until X.columns).map(i => math.random).toArray).mulPointwise(colRanges) + colMins
    }
  }

  class ZScoreFeatureNormalizer extends FeatureNormalizer {

    def normalizer(X: Matrix[Double]): Normalize = new Normalize {

      lazy val μs = X.columnMeans
      lazy val σ2s = std(X)
      val nd = zscore(X)

      def normalizedData: Matrix[Double] = nd

      def apply(features: Seq[Double]): Matrix[Double] =
        (matrix(1, features.length, features.toArray) - μs).divPointwise(σ2s)

      def unapply(featureRow: Matrix[Double]): Seq[Double] =
        (featureRow.mulPointwise(σ2s) + μs).toList

      def random(): Matrix[Double] =
        matrix(1, X.columns, (0 until X.columns).map(i => util.Random.nextGaussian).toArray)
    }
  }

  class PCAFeatureNormalizer(cutoff: Double) extends FeatureNormalizer {

    def normalizer(X: Matrix[Double]): Normalize = new Normalize {

      lazy val μs = X.columnMeans
      lazy val σ2s = std(X)
      val zd = zscore(X)
      val (u, s) = pca(zd)
      val k = numComponentsForCutoff(s, cutoff)
      val Uk = u(0 until u.rows, 0 until k)

      def normalizedData: Matrix[Double] = zd ⨯ Uk

      def apply(features: Seq[Double]): Matrix[Double] =
        (matrix(1, features.length, features.toArray) - μs).divPointwise(σ2s) ⨯ Uk

      def unapply(featureRow: Matrix[Double]): Seq[Double] =
        ((featureRow ⨯ Uk.t).mulPointwise(σ2s) + μs).toList

      def random(): Matrix[Double] =
        matrix(1, X.columns, (0 until X.columns).map(i => util.Random.nextGaussian).toArray) ⨯ Uk

      // (truncatedSigmas.mulPointwise(featureRow) + truncatedMeans).toList
      // val truncatedSigmas = σ2s ⨯ Uk
      // val truncatedMeans = μs ⨯ Uk
      // ns = (fs - μs) .* σ2s ⨯ Uk
      // (ns ⨯ Uk') ./ σ2s + μs  = fs
    }
  }

}
