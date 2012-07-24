package axle.ml

object NaiveBayesUtil {

  import collection._

  def mapReduce[D, K, V](data: Iterator[D], mapper: D => Seq[(K, V)], reducer: (V, V) => V): immutable.Map[K, V] =
    data
      .map(mapper(_))
      .flatMap(x => x)
      .toList // TODO inefficient
      .groupBy(_._1)
      .map(kv => (kv._1, kv._2.map(_._2).reduce(reducer)))

  // Note: with scalaz, combineMaps just becomes map1 |+| map2
  def combineMaps[K](map1: immutable.Map[K, Int], map2: immutable.Map[K, Int]): immutable.Map[K, Int] = map1 ++ map2.map({ case (k, v) => k -> (v + map1.getOrElse(k, 0)) })

}

class NaiveBayesClassifier[T](data: Seq[T],
  featureSpace: List[(String, List[String])], extractFeatures: T => List[String], extractLabel: T => String) {

  import NaiveBayesUtil._

  val featureNames = featureSpace.map(_._1)

  val featureMap = featureSpace.toMap

  val featureTally = mapReduce(
    data.iterator,
    (t: T) => featureNames.zip(extractFeatures(t)).map({ case (f, fv) => ((extractLabel(t), f, fv), 1) }),
    (x: Int, y: Int) => x + y
  ).withDefaultValue(0)

  val unsmoothedLabelTally = mapReduce(
    data.iterator,
    (t: T) => List((extractLabel(t), 1)),
    (x: Int, y: Int) => x + y
  )

  val smoothing = unsmoothedLabelTally.keys.flatMap(lv => featureNames.map(featureName => (lv, featureMap(featureName).size))).toMap

  //  val smoothing = (for {
  //    lv <- unsmoothedLabelTally.keys
  //    featureName <- featureNames
  //  } yield (lv, featureMap(featureName).size)).toMap

  val labelTally = combineMaps(unsmoothedLabelTally, smoothing).withDefaultValue(0)

  val totalCount = labelTally.values.sum

  def classify(datum: T): (String, Double) = labelTally.keys.map(lv => {
    val logP = featureNames
      .zip(extractFeatures(datum))
      .map({ case (f, fv) => math.log(featureTally((lv, f, fv)).toDouble / labelTally(lv)) })
      .reduce(_ + _)
    (lv, (labelTally(lv).toDouble / totalCount) * math.exp(logP))
  }).maxBy(_._2)

}
