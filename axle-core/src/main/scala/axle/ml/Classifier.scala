package axle.ml

import spire.math._
import spire.implicits._
import spire.algebra._
import axle._
import axle.matrix.JblasMatrixModule
import axle.algebra._
import axle.algebra.Semigroups._
import scala.reflect.ClassTag

abstract class Classifier[DATA: ClassTag, CLASS: Order: Eq: ClassTag] extends Function1[DATA, CLASS] {

  def apply(d: DATA): CLASS

  def classes: IndexedSeq[CLASS]

  /**
   * For a given class (label value), predictedVsActual returns a tally of 4 cases:
   *
   * 1. true positive
   * 2. false positive
   * 3. false negative
   * 4. true negative
   *
   */

  private[this] def predictedVsActual[F[_]: Aggregatable: Functor](
    data: F[DATA],
    classExtractor: DATA => CLASS,
    k: CLASS): (Int, Int, Int, Int) = {

    val func = implicitly[Functor[F]]
    val agg = implicitly[Aggregatable[F]]

    val scores = func.map(data)(d => {
      val actual: CLASS = classExtractor(d)
      val predicted: CLASS = this(d)
      (actual === k, predicted === k) match {
        case (true, true)   => (1, 0, 0, 0) // true positive
        case (false, true)  => (0, 1, 0, 0) // false positive
        case (false, false) => (0, 0, 1, 0) // false negative
        case (true, false)  => (0, 0, 0, 1) // true negative
      }
    })

    Σ(scores)
  }

  def performance[F[_]: Aggregatable: Functor](
    data: F[DATA],
    classExtractor: DATA => CLASS,
    k: CLASS): ClassifierPerformance[Rational] = {

    val (tp, fp, fn, tn) = predictedVsActual(data, classExtractor, k)

    ClassifierPerformance[Rational](
      Rational(tp, tp + fp), // precision
      Rational(tp, tp + fn), // recall
      Rational(tn, tn + fp), // specificity aka "true negative rate"
      Rational(tp + tn, tp + tn + fp + fn) // accuracy
      )
  }

  def confusionMatrix[L: Order: ClassTag, F[_]: Functor: Finite: SetFrom: MapReducible: MapFrom](
    data: F[DATA],
    labelExtractor: DATA => L): ConfusionMatrix[DATA, CLASS, L, F] =
    new ConfusionMatrix(this, data, labelExtractor) with JblasMatrixModule

}