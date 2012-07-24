package axle.ml

class NaiveBayesClassifier[D](data: Seq[D],
  featureSpace: List[(String, List[String])],
  classValues: List[String],
  featureExtractor: D => List[String],
  classExtractor: D => String) {

  import axle.ScalaMapReduce._
  import axle.Enrichments._
  import collection._
  import scalaz._
  import Scalaz._

  val featureNames = featureSpace.map(_._1)

  val featureMap = featureSpace.toMap

  val numFeatureValues = featureSpace.map(kv => kv._2.size).sum
  
  val featureTally = mapReduce(
    data.iterator,
    mapper = (d: D) => featureNames.zip(featureExtractor(d)).map({ case (f, fv) => ((classExtractor(d), f, fv), 1) }),
    reducer = (x: Int, y: Int) => x + y
  ).withDefaultValue(0)

  val classTally = mapReduce(
    data.iterator,
    mapper = (d: D) => List((classExtractor(d), 1)),
    reducer = (x: Int, y: Int) => x + y
  ).withDefaultValue(1) // to avoid division by zero

  val totalCount = classTally.values.sum

  def argmax[K](s: Iterable[(K, Double)]): K = s.maxBy(_._2)._1

  def predict(datum: D): String = argmax(classValues.map(c => {
    val fs = featureExtractor(datum)
    val probC = (classTally(c).toDouble / totalCount)
    val probFsGivenC = (0 until fs.length) Π (i => featureTally(c, featureNames(i), fs(i)).toDouble / classTally(c))
    (c, probC * probFsGivenC)
  }))

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

  //  val smoothing = (for {
  //    lv <- unsmoothedLabelTally.keys
  //    featureName <- featureNames
  //  } yield (lv, featureMap(featureName).size)).toMap
