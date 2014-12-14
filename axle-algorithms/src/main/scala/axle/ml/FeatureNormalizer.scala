package axle.ml

import axle.algebra.Matrix
import axle.syntax.matrix._

abstract class Normalize[M[_]: Matrix] extends (Seq[Double] => M[Double]) {

  def normalizedData: M[Double]

  def apply(featureList: Seq[Double]): M[Double]

  def unapply(featureRow: M[Double]): Seq[Double]

  def random(): M[Double]
}

case class IdentityFeatureNormalizer[M[_]](X: M[Double])(implicit ev: Matrix[M]) extends Normalize[M] {

  def normalizedData: M[Double] = X

  def apply(featureList: Seq[Double]): M[Double] =
    ev.matrix(1, featureList.length, featureList.toArray)

  def unapply(featureRow: M[Double]): Seq[Double] =
    featureRow.toList

  def random(): M[Double] = ev.matrix(1, X.columns, (0 until X.columns).map(i => math.random).toArray)
}

case class LinearFeatureNormalizer[M[_]](X: M[Double])(implicit ev: Matrix[M]) extends Normalize[M] {

  val colMins = X.columnMins
  val colRanges = X.columnMaxs - colMins
  val nd = X.subRowVector(colMins).divRowVector(colRanges)

  def normalizedData: M[Double] = nd

  def apply(features: Seq[Double]): M[Double] =
    ev.matrix(1, features.length, features.toArray).subRowVector(colMins).divPointwise(colRanges)

  def unapply(featureRow: M[Double]): Seq[Double] =
    (featureRow.mulPointwise(colRanges) + colMins).toList

  def random(): M[Double] =
    ev.matrix(1, X.columns, (0 until X.columns).map(i => math.random).toArray).mulPointwise(colRanges) + colMins

}

case class ZScoreFeatureNormalizer[M[_]](X: M[Double])(implicit ev: Matrix[M]) extends Normalize[M] {

  lazy val μs = X.columnMeans
  lazy val σ2s = std(X)
  val nd = zscore(X)

  def normalizedData: M[Double] = nd

  def apply(features: Seq[Double]): M[Double] =
    (ev.matrix(1, features.length, features.toArray) - μs).divPointwise(σ2s)

  def unapply(featureRow: M[Double]): Seq[Double] =
    (featureRow.mulPointwise(σ2s) + μs).toList

  def random(): M[Double] =
    ev.matrix(1, X.columns, (0 until X.columns).map(i => util.Random.nextGaussian).toArray)

}

case class PCAFeatureNormalizer[M[_]](cutoff: Double, X: M[Double])(implicit ev: Matrix[M]) extends Normalize[M] {

  lazy val μs = X.columnMeans
  lazy val σ2s = std(X)
  val zd = zscore(X)
  val (u, s) = pca(zd, 0.95)
  val k = numComponentsForCutoff(s, cutoff)
  val Uk = u.slice(0 until u.rows, 0 until k)

  def normalizedData: M[Double] = zd ⨯ Uk

  def apply(features: Seq[Double]): M[Double] =
    (ev.matrix(1, features.length, features.toArray) - μs).divPointwise(σ2s) ⨯ Uk

  def unapply(featureRow: M[Double]): Seq[Double] =
    ((featureRow ⨯ Uk.t).mulPointwise(σ2s) + μs).toList

  def random(): M[Double] =
    ev.matrix(1, X.columns, (0 until X.columns).map(i => util.Random.nextGaussian).toArray) ⨯ Uk

  // (truncatedSigmas.mulPointwise(featureRow) + truncatedMeans).toList
  // val truncatedSigmas = σ2s ⨯ Uk
  // val truncatedMeans = μs ⨯ Uk
  // ns = (fs - μs) .* σ2s ⨯ Uk
  // (ns ⨯ Uk') ./ σ2s + μs  = fs
}
