package axle.ml

/**
 * "performance" returns four measures of classification performance
 * for the given class.
 *
 * They are:
 *
 * 1. Precision
 * 2. Recall
 * 3. Specificity
 * 4. Accuracy
 *
 * See http://en.wikipedia.org/wiki/Precision_and_recall for more information.
 *
 * http://en.wikipedia.org/wiki/F1_score
 *
 */

import spire.math._
import spire.implicits._
import spire.algebra._
import axle.Show

case class ClassifierPerformance[N: Field](
  precision: N,
  recall: N,
  specificity: N,
  accuracy: N) {

  def f1Score: N = 2 * (precision * recall) / (precision + recall)

  def fScore(β: Int = 1): N =
    (1 + (β * β)) * (precision * recall) / ((β * β * precision) + recall)

}

object ClassifierPerformance {

  implicit def showCP[N: Field]: Show[ClassifierPerformance[N]] =
    new Show[ClassifierPerformance[N]] {

      def text(cp: ClassifierPerformance[N]): String = {
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
