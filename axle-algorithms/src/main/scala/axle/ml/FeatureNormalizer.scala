package axle.ml

import axle.algebra.LinearAlgebra
import axle.syntax.linearalgebra._
import spire.implicits._

trait Normalize[M] extends (Seq[Double] => M) {

  def normalizedData: M

  def apply(featureList: Seq[Double]): M

  def unapply(featureRow: M): Seq[Double]

  def random(): M
}

case class IdentityFeatureNormalizer[M](X: M)(implicit la: LinearAlgebra[M, Int, Int, Double])
  extends Normalize[M] {

  def normalizedData: M = X

  def apply(featureList: Seq[Double]): M =
    la.matrix(1, featureList.length, featureList.toArray)

  def unapply(featureRow: M): Seq[Double] =
    featureRow.toList

  def random(): M = la.matrix(1, X.columns, (0 until X.columns).map(i => math.random).toArray)
}

case class LinearFeatureNormalizer[M](X: M)(implicit la: LinearAlgebra[M, Int, Int, Double])
  extends Normalize[M] {

  //implicit val module = la.module
  implicit val ring = la.ring

  val colMins = X.columnMins
  val colRanges = X.columnMaxs - colMins
  val nd = X.subRowVector(colMins).divRowVector(colRanges)

  def normalizedData: M = nd

  def apply(features: Seq[Double]): M =
    la.matrix(1, features.length, features.toArray).subRowVector(colMins).divPointwise(colRanges)

  def unapply(featureRow: M): Seq[Double] =
    (featureRow.mulPointwise(colRanges) + colMins).toList

  def random(): M =
    la.matrix(1, X.columns, (0 until X.columns).map(i => math.random).toArray).mulPointwise(colRanges) + colMins

}

case class ZScoreFeatureNormalizer[M](X: M)(implicit la: LinearAlgebra[M, Int, Int, Double])
  extends Normalize[M] {

  //implicit val ring = la.ring
  implicit val module = la.module

  lazy val μs = X.columnMeans
  lazy val σ2s = std(X)
  val nd = zscore(X)

  def normalizedData: M = nd

  def apply(features: Seq[Double]): M =
    (la.matrix(1, features.length, features.toArray) - μs).divPointwise(σ2s)

  def unapply(featureRow: M): Seq[Double] =
    (featureRow.mulPointwise(σ2s) + μs).toList

  def random(): M =
    la.matrix(1, X.columns, (0 until X.columns).map(i => util.Random.nextGaussian).toArray)

}

case class PCAFeatureNormalizer[M](cutoff: Double, X: M)(implicit la: LinearAlgebra[M, Int, Int, Double])
  extends Normalize[M] {

  //implicit val module = la.module
  implicit val ring = la.ring

  lazy val μs = X.columnMeans
  lazy val σ2s = std(X)
  val zd = zscore(X)
  val (u, s) = pca(zd, 0.95)
  val k = numComponentsForCutoff(s, cutoff)
  val Uk = u.slice(0 until u.rows, 0 until k)

  def normalizedData: M = zd * Uk

  def apply(features: Seq[Double]): M =
    (la.matrix(1, features.length, features.toArray) - μs).divPointwise(σ2s) * Uk

  def unapply(featureRow: M): Seq[Double] =
    ((featureRow * Uk.t).mulPointwise(σ2s) + μs).toList

  def random(): M =
    la.matrix(1, X.columns, (0 until X.columns).map(i => util.Random.nextGaussian).toArray) * Uk

  // (truncatedSigmas.mulPointwise(featureRow) + truncatedMeans).toList
  // val truncatedSigmas = σ2s * Uk
  // val truncatedMeans = μs * Uk
  // ns = (fs - μs) .* σ2s * Uk
  // (ns * Uk') ./ σ2s + μs  = fs
}
