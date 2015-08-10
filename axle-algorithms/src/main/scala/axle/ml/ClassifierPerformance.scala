package axle.ml

import axle.Show
import axle._
import axle.algebra.Σ
import axle.algebra.Aggregatable
import axle.algebra.LinearAlgebra
import axle.algebra.Functor
import axle.algebra.Finite
import axle.algebra.SetFrom
import axle.algebra.MapReducible
import axle.algebra.MapFrom
import axle.algebra.FunctionPair
import axle.algebra.Semigroups._
import axle.syntax.functor._
import axle.syntax.finite._
import spire.math._
import spire.implicits._
import spire.algebra._
import spire.compat.ordering

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

case class ClassifierPerformance[N, DATA, F, G](
    data: F,
    retrieve: DATA => Boolean,
    relevant: DATA => Boolean)(
        implicit functor: Functor[F, DATA, (N, N, N, N), G],
        agg: Aggregatable[G, (N, N, N, N), (N, N, N, N)],
        field: Field[N]) {

  import field._

  val scores = data.map { d =>
    (relevant(d), retrieve(d)) match {
      case (true, true)   => (one, zero, zero, zero) // true positive
      case (false, true)  => (zero, one, zero, zero) // false positive
      case (false, false) => (zero, zero, one, zero) // true negative
      case (true, false)  => (zero, zero, zero, one) // false negative
    }
  }

  val (tp, fp, tn, fn) = Σ[(N, N, N, N), G](scores)

  val precision: N = tp / (tp + fp)

  val recall: N = tp / (tp + fn)

  val specificity: N = tn / (tn + fp)

  val accuracy: N = (tp + tn) / (tp + tn + fp + fn)

  def f1Score: N = 2 * (precision * recall) / (precision + recall)

  def fScore(β: Double = 1d): N =
    (1 + (β * β)) * (precision * recall) / ((β * β * precision) + recall)

}

object ClassifierPerformance {

  implicit def showCP[N, DATA, F, G]: Show[ClassifierPerformance[N, DATA, F, G]] =
    new Show[ClassifierPerformance[N, DATA, F, G]] {

      def text(cp: ClassifierPerformance[N, DATA, F, G]): String = {
        import cp._
        s"""Precision   $precision
Recall      $recall
Specificity $specificity
Accuracy    $accuracy
F1 Score    $f1Score
"""
      }

    }

}
