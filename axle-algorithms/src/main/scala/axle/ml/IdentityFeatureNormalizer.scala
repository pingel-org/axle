package axle.ml

import spire.implicits._
import axle.algebra.LinearAlgebra
import axle.syntax.linearalgebra._

case class IdentityFeatureNormalizer[M](X: M)(implicit la: LinearAlgebra[M, Int, Int, Double])
  extends Normalize[M] {

  def normalizedData: M = X

  def apply(featureList: Seq[Double]): M =
    la.fromColumnMajorArray(1, featureList.length, featureList.toArray)

  def unapply(featureRow: M): Seq[Double] =
    featureRow.toList

  def random(): M = la.fromColumnMajorArray(1, X.columns, (0 until X.columns).map(i => math.random).toArray)
}
