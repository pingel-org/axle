package axle.ml

import spire.implicits._
import spire.implicits.additiveSemigroupOps
import axle.algebra.LinearAlgebra
import axle.syntax.linearalgebra._

case class LinearFeatureNormalizer[M](X: M)(implicit la: LinearAlgebra[M, Int, Int, Double])
  extends Normalize[M] {

  implicit val add = la.additive

  val colMins = X.columnMins

  val colRanges = X.columnMaxs - colMins
  val nd = X.subRowVector(colMins).divRowVector(colRanges)

  def normalizedData: M = nd

  def apply(features: Seq[Double]): M =
    la.fromColumnMajorArray(1, features.length, features.toArray).subRowVector(colMins).divPointwise(colRanges)

  def unapply(featureRow: M): Seq[Double] =
    (featureRow.mulPointwise(colRanges) + colMins).toList

  def random(): M =
    la.fromColumnMajorArray(1, X.columns, (0 until X.columns).map(i => math.random).toArray).mulPointwise(colRanges) + colMins

}
