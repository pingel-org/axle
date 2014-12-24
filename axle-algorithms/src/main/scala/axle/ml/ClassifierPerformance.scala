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
import spire.math._
import spire.implicits._
import spire.algebra._

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
 * The "classification task" is defined by three arguments:
 *
 * 1) predict   : given a datum computes a value
 * 2) isIn      : given the value, determines whether the value is "in" the retrieved set
 * 3) shouldBeIn: given the value, determines whether the value is *actually* "in" the retrieved set
 *
 * See http://en.wikipedia.org/wiki/Precision_and_recall for more information.
 *
 * http://en.wikipedia.org/wiki/F1_score
 *
 */

case class ClassifierPerformance[N: Field, DATA, F[_]: Aggregatable: Functor, CLASS](
  data: F[DATA],
  predict: DATA => CLASS,
  isIn: CLASS => Boolean,
  shouldBeIn: CLASS => Boolean) {

  val field = implicitly[Field[N]]

  val scores = data.map { d =>
    val prediction = predict(d)
    (shouldBeIn(prediction), isIn(prediction)) match {
      case (true, true)   => (1, 0, 0, 0) // true positive
      case (false, true)  => (0, 1, 0, 0) // false positive
      case (false, false) => (0, 0, 1, 0) // false negative
      case (true, false)  => (0, 0, 0, 1) // true negative
    }
  }

  val (tpInt, fpInt, fnInt, tnInt) = Σ(scores)
  val (tp, fp, fn, tn) = (field.fromInt(tpInt), field.fromInt(fpInt), field.fromInt(fnInt), field.fromInt(tnInt))

  val precision: N = tp / (tp + fp)

  val recall: N = tp / (tp + fn)

  val specificity: N = tn / (tn + fp)

  val accuracy: N = (tp + tn) / (tp + tn + fp + fn)

  def f1Score: N = 2 * (precision * recall) / (precision + recall)

  def fScore(β: Int = 1): N =
    (1 + (β * β)) * (precision * recall) / ((β * β * precision) + recall)

}

object ClassifierPerformance {

  implicit def showCP[N, DATA, F[_], CLASS]: Show[ClassifierPerformance[N, DATA, F, CLASS]] =
    new Show[ClassifierPerformance[N, DATA, F, CLASS]] {

      def text(cp: ClassifierPerformance[N, DATA, F, CLASS]): String = {
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
