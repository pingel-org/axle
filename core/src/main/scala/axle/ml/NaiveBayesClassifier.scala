package axle.ml

import axle.InformationTheory._
import axle.Statistics._

class NaiveBayesClassifier[D](data: Seq[D],
  Fs: List[RandomVariableNoInput[String]],
  C: RandomVariable[String],
  featureExtractor: D => List[String],
  classExtractor: D => String) {

  import axle.ScalaMapReduce._
  import axle.Enrichments._
  import collection._
  import scalaz._
  import Scalaz._

  val featureNames = Fs.map(_.name)

  val N = featureNames.size

  def argmax[K](s: Iterable[(K, () => Double)]): K = s.maxBy(_._2())._1

  // TODO no probability should ever be 0

  val featureTally = mapReduce(data.iterator,
    mapper = (d: D) => {
      val fs = featureExtractor(d)
      (0 until fs.length).map(i => (fs(i), 1)) // ((classExtractor(d), (featureNames(i), fs(i))), 1))
    },
    reducer = (x: Int, y: Int) => x + y
  ).withDefaultValue(0)

  val ftd = new TallyDistributionNoInput(Fs(0), featureTally)
  val fd = new RandomVariableNoInput(Fs(0).name, values = Fs(0).getValues, distribution = Some(ftd))
  val pfd = P(fd eq "bar")

  val classTally = mapReduce(data.iterator,
    mapper = (d: D) => List((classExtractor(d), 1)),
    reducer = (x: Int, y: Int) => x + y
  ).withDefaultValue(1) // to avoid division by zero

  def predict(d: D): String = {
    val fs = featureExtractor(d)
    argmax(C.getValues.get.map(c => (c, P(C eq c) * (0 until N).Π((i: Int) => P((Fs(i) eq fs(i)) | (C eq c))))))
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

  def predictedVsActual(dit: Iterator[D], k: String): (Int, Int, Int, Int) = dit.map(d => {
    val actual = classExtractor(d)
    val predicted = predict(d)
    (actual === k, predicted === k) match {
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

  def performance(dit: Iterator[D], k: String): (Double, Double, Double, Double) = {

    val (tp, fp, fn, tn) = predictedVsActual(dit, k)

    val precision = tp.toDouble / (tp + fp)
    val recall = tp.toDouble / (tp + fn)
    val specificity = tn.toDouble / (tn + fp) // aka "true negative rate"
    val accuracy = (tp + tn).toDouble / (tp + tn + fp + fn)

    (precision, recall, specificity, accuracy)
  }

}

// def probC(c: String): Double = classTally(c).toDouble / totalCount
// def probFsGivenC(fs: List[String], c: String) = (0 until fs.length) Π ((i: Int) => featureTally(c, featureNames(i), fs(i)).toDouble / classTally(c))
