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

case class ClassifierPerformance[N: Field, DATA, F[_]: Aggregatable: Functor, CLASS: Eq](
  data: F[DATA],
  classifier: DATA => CLASS,
  classExtractor: DATA => CLASS,
  k: CLASS) {

  val field = implicitly[Field[N]]

  val scores = data.map(d => {
    val actual = classExtractor(d)
    val predicted = classifier(d)
    (actual === k, predicted === k) match {
      case (true, true)   => (1, 0, 0, 0) // true positive
      case (false, true)  => (0, 1, 0, 0) // false positive
      case (false, false) => (0, 0, 1, 0) // false negative
      case (true, false)  => (0, 0, 0, 1) // true negative
    }
  })

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
