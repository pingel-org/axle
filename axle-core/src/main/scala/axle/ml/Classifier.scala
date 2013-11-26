package axle.ml

abstract class Classifier[DATA, CLASS: Ordering] extends Function1[DATA, CLASS] {

  def apply(d: DATA): CLASS

  def classes(): IndexedSeq[CLASS]
  
  /**
   * For a given class (label value), predictedVsActual returns a tally of 4 cases:
   *
   * 1. true positive
   * 2. false positive
   * 3. false negative
   * 4. true negative
   *
   */

  import axle.algebra._
  import Semigroups._

  private[this] def predictedVsActual(data: Seq[DATA], classExtractor: DATA => CLASS, k: CLASS): (Int, Int, Int, Int) = data.map(d => {
    val actual = classExtractor(d)
    val predicted = this(d)
    (actual === k, predicted === k) match {
      case (true, true) => (1, 0, 0, 0) // true positive
      case (false, true) => (0, 1, 0, 0) // false positive
      case (false, false) => (0, 0, 1, 0) // false negative
      case (true, false) => (0, 0, 0, 1) // true negative
    }
  }).reduce(_ |+| _)

  def performance(data: Seq[DATA], classExtractor: DATA => CLASS, k: CLASS) = {

    val (tp, fp, fn, tn) = predictedVsActual(data, classExtractor, k)

    ClassifierPerformance(
      tp.toDouble / (tp + fp), // precision
      tp.toDouble / (tp + fn), // recall
      tn.toDouble / (tn + fp), // specificity aka "true negative rate"
      (tp + tn).toDouble / (tp + tn + fp + fn) // accuracy
    )
  }

  def confusionMatrix[L: Ordering](data: Seq[DATA], labelExtractor: DATA => L) = new ConfusionMatrix(this, data, labelExtractor)
  
  
}
