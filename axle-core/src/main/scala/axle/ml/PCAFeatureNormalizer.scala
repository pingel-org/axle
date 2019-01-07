package axle.ml

import spire.implicits._

import axle.algebra.LinearAlgebra
import axle.syntax.linearalgebra._

case class PCAFeatureNormalizer[M](cutoff: Double, X: M)(implicit la: LinearAlgebra[M, Int, Int, Double])
  extends Normalize[M] {

  implicit val mul = la.multiplicative
  implicit val additive = la.additive
  // import spire.implicits.DoubleAlgebra

  lazy val μs = X.columnMeans
  lazy val σ2s = std(X)
  val zd = zscore(X)
  val (u, s) = pca(zd)
  val k = numComponentsForCutoff(s, cutoff)(la, spire.algebra.Field[Double])
  val Uk = u.slice(0 until u.rows, 0 until k)

  def normalizedData: M = zd * Uk

  def apply(features: Seq[Double]): M =
    (la.fromColumnMajorArray(1, features.length, features.toArray) - μs).divPointwise(σ2s) * Uk

  def unapply(featureRow: M): Seq[Double] =
    ((featureRow * Uk.t).mulPointwise(σ2s) + μs).toList

  //  def random(): M =
  //    la.fromColumnMajorArray(1, X.columns, (0 until X.columns).map(i => util.Random.nextGaussian).toArray) * Uk

  // (truncatedSigmas.mulPointwise(featureRow) + truncatedMeans).toList
  // val truncatedSigmas = σ2s * Uk
  // val truncatedMeans = μs * Uk
  // ns = (fs - μs) .* σ2s * Uk
  // (ns * Uk') ./ σ2s + μs  = fs
}