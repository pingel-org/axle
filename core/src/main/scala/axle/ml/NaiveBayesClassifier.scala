package axle.ml

class NaiveBayesClassifier[D](data: Seq[D],
  featureSpace: List[(String, List[String])],
  fs: D => List[String], // feature extractor
  c: D => String) { // class extractor

  import axle.ScalaMapReduce._
  import collection._
  import scalaz._
  import Scalaz._

  val featureNames = featureSpace.map(_._1)

  val featureMap = featureSpace.toMap

  val featureTally = mapReduce(
    data.iterator,
    mapper = (d: D) => featureNames.zip(fs(d)).map({ case (f, fv) => ((c(d), f, fv), 1) }),
    reducer = (x: Int, y: Int) => x + y
  ).withDefaultValue(0)

  val unsmoothedLabelTally = mapReduce(
    data.iterator,
    mapper = (d: D) => List((c(d), 1)),
    reducer = (x: Int, y: Int) => x + y
  )

  val smoothing = unsmoothedLabelTally.keys.flatMap(lv => featureNames.map(featureName => (lv, featureMap(featureName).size))).toMap

  val cTally = (unsmoothedLabelTally |+| smoothing).withDefaultValue(0)

  val totalCount = cTally.values.sum

  def predict(datum: D): String = cTally.keys.map(lv => {
    val logP = featureNames
      .zip(fs(datum))
      .map({ case (f, fv) => math.log(featureTally((lv, f, fv)).toDouble / cTally(lv)) })
      .reduce(_ + _)
    (lv, (cTally(lv).toDouble / totalCount) * math.exp(logP))
  }).maxBy(_._2)._1

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
    val actual = c(d)
    val predicted = predict(d)
    (actual === k, predicted === k) match {
      case (true, true) => (1, 0, 0, 0) // true positive
      case (false, true) => (0, 1, 0, 0) // false positive
      case (false, false) => (0, 0, 1, 0) // false negative
      case (true, false) => (0, 0, 0, 1) // true negative
    }
  }).foldLeft((0, 0, 0, 0))(_ |+| _)

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


  // def combineMaps[K](map1: immutable.Map[K, Int], map2: immutable.Map[K, Int]): immutable.Map[K, Int] = map1 ++ map2.map({ case (k, v) => k -> (v + map1.getOrElse(k, 0)) })
  
  //  val smoothing = (for {
  //    lv <- unsmoothedLabelTally.keys
  //    featureName <- featureNames
  //  } yield (lv, featureMap(featureName).size)).toMap
