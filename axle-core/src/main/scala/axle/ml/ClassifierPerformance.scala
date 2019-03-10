package axle.ml

import cats.Functor
import cats.Show
import cats.implicits._

import spire.algebra._
import spire.implicits.additiveSemigroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.implicits.multiplicativeGroupOps

import axle.algebra.Talliable
import axle.syntax.talliable._

/**
 * ClassifierPerformance computes measures of classification performance
 *
 * They are:
 *
 *   * Precision
 *   * Recall
 *   * Specificity
 *   * Accuracy
 *   * F1
 *
 * The (boolean) "classification task" is defined by two arguments:
 *
 * 1) predict: given a datum, determines whether the value is "in" the retrieved set
 * 2) actual : given a datum, determines whether the value is *actually* "in" the retrieved set
 *
 * See http://en.wikipedia.org/wiki/Precision_and_recall for more information.
 *
 * http://en.wikipedia.org/wiki/F1_score
 *
 *
 *
 */

case class ClassifierPerformance[N, DATA, F[_]](
  data:     F[DATA],
  retrieve: DATA => Boolean,
  relevant: DATA => Boolean)(
  implicit
  functor: Functor[F],
  talliable: Talliable[F],
  field:   Field[N]) {

  sealed trait Bucket
  object TruePositive extends Bucket
  object FalsePositive extends Bucket
  object TrueNegative extends Bucket
  object FalseNegative extends Bucket

  import field.one
  val two = one + one

  val relevantRetrievedToBucket: Map[(Boolean, Boolean), Bucket] = Map(
    (true, true)   -> TruePositive,
    (false, true)  -> FalsePositive,
    (false, false) -> TrueNegative,
    (true, false)  -> FalseNegative
  )

  val buckets: F[Bucket] = data.map { d => relevantRetrievedToBucket((relevant(d), retrieve(d))) }

  val bucketTally: Map[Bucket, N] = buckets.tally

  val tp = bucketTally(TruePositive)
  val fp = bucketTally(FalsePositive)
  val tn = bucketTally(TrueNegative)
  val fn = bucketTally(FalseNegative)  

  val precision: N = tp / (tp + fp)

  val recall: N = tp / (tp + fn)

  val specificity: N = tn / (tn + fp)

  val accuracy: N = (tp + tn) / (tp + tn + fp + fn)

  def f1Score: N = two * (precision * recall) / (precision + recall)

  def fScore(β: N = one): N =
    (one + (β * β)) * (precision * recall) / ((β * β * precision) + recall)

}

object ClassifierPerformance {

  implicit def showCP[N, DATA, F[_]]: Show[ClassifierPerformance[N, DATA, F]] = cp => {
    import cp._
    s"""Precision   $precision
Recall      $recall
Specificity $specificity
Accuracy    $accuracy
F1 Score    $f1Score
"""
  }

}
