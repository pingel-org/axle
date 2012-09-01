package axle.ml

import axle.InformationTheory._
import axle.Statistics._
import axle.stats._

class NaiveBayesClassifier[D, TF, TC](data: Seq[D],
  pFs: List[RandomVariable[TF]],
  pC: RandomVariable[TC],
  featureExtractor: D => List[TF],
  classExtractor: D => TC) {

  import axle.ScalaMapReduce._
  import axle._
  import collection._
  import scalaz._
  import Scalaz._

  val featureNames = pFs.map(_.getName)

  val N = featureNames.size

  def argmax[K](ks: Iterable[K], f: K => () => Double): K = ks.map(k => (k, f(k)())).maxBy(_._2)._1

  // TODO no probability should ever be 0

  val featureTally: immutable.Map[(TC, String, TF), Int] = mapReduce(data.iterator,
    mapper = (d: D) => {
      val fs = featureExtractor(d)
      (0 until fs.length).map(i => ((classExtractor(d), featureNames(i), fs(i)), 1))
    },
    reducer = (x: Int, y: Int) => x + y
  ).withDefaultValue(0)

  val classTally: immutable.Map[TC, Int] = mapReduce(data.iterator,
    mapper = (d: D) => List((classExtractor(d), 1)),
    reducer = (x: Int, y: Int) => x + y
  ).withDefaultValue(1) // to avoid division by zero

  val C = new RandomVariable0(
    pC.getName,
    values = pC.getValues,
    distribution = Some(new TallyDistribution0(classTally)))

  val Fs = pFs.map(pF => new RandomVariable1(
    pF.getName,
    values = pF.getValues,
    grv = C,
    distribution = Some(new TallyDistribution1(
      featureTally
        .filter(_._1._2 == pF.getName)
        .map(kv => ((kv._1._3, kv._1._1), kv._2))
        .withDefaultValue(0)))))

  def predict(d: D): TC = {
    val fs = featureExtractor(d)
    argmax(C, (c: TC) => P(C eq c) * (0 until N).Π(i => P((Fs(i) eq fs(i)) | (C eq c))))
  }

  /**
   * For a given class (label value), predictedVsActual returns a tally of 4 cases:
   *
   * 1. true positive
   * 2. false positive
   * 3. false negative
   * 4. true negative
   *
   */

  def predictedVsActual(dit: Iterator[D], k: TC): (Int, Int, Int, Int) = dit.map(d => {
    val actual = classExtractor(d)
    val predicted = predict(d)
    (actual == k, predicted == k) match { // TODO use type-safe equality (===)
      case (true, true) => (1, 0, 0, 0) // true positive
      case (false, true) => (0, 1, 0, 0) // false positive
      case (false, false) => (0, 0, 1, 0) // false negative
      case (true, false) => (0, 0, 0, 1) // true negative
    }
  }).reduce(_ |+| _)

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
   */

  def performance(dit: Iterator[D], k: TC): (Double, Double, Double, Double) = {

    val (tp, fp, fn, tn) = predictedVsActual(dit, k)

    val precision = tp.toDouble / (tp + fp)
    val recall = tp.toDouble / (tp + fn)
    val specificity = tn.toDouble / (tn + fp) // aka "true negative rate"
    val accuracy = (tp + tn).toDouble / (tp + tn + fp + fn)

    (precision, recall, specificity, accuracy)
  }

}
