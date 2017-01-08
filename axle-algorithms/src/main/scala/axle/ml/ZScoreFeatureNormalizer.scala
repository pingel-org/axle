package axle.ml

import spire.implicits._
import axle.algebra.LinearAlgebra
import axle.syntax.linearalgebra._

case class ZScoreFeatureNormalizer[M](X: M)(implicit la: LinearAlgebra[M, Int, Int, Double])
  extends Normalize[M] {

  //implicit val ring = la.ring
  implicit val module = la.module

  lazy val μs = X.columnMeans
  lazy val σ2s = std(X)
  val nd = zscore(X)

  def normalizedData: M = nd

  def apply(features: Seq[Double]): M =
    (la.fromColumnMajorArray(1, features.length, features.toArray) - μs).divPointwise(σ2s)

  def unapply(featureRow: M): Seq[Double] =
    (featureRow.mulPointwise(σ2s) + μs).toList

  def random(): M =
    la.fromColumnMajorArray(1, X.columns, (0 until X.columns).map(i => util.Random.nextGaussian).toArray)

}