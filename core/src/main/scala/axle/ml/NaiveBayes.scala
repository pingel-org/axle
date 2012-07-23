package axle.ml

class NaiveBayes[T](featureNames: List[String], featureSpace: Map[String, List[String]], extractFeatures: T => List[String], extractLabel: T => String) {

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

  def train(data: Seq[T]): (Map[(String, String, String), Int], Map[String, Int], Int) = {

    val featureTally = mapReduce[T, (String, String, String), Int](
      data.iterator,
      (t: T) => featureNames.zip(extractFeatures(t)).map({ case (f, fv) => ((extractLabel(t), f, fv), 1) }),
      { _ + _ }
    )

    val labelTally = mapReduce[T, String, Int](data.iterator, (t: T) => List((extractLabel(t), 1)), { _ + _ })

    val smoothing = labelTally.keys.flatMap(lv => featureNames.map(featureName => (lv, featureSpace(featureName).size))).toMap

    val totalCount = labelTally.values.sum

    (featureTally.withDefaultValue(0), combineMaps(labelTally, smoothing).withDefaultValue(0), totalCount)
  }

  def classify(datum: T, featureTally: Map[(String, String, String), Int], labelTally: Map[String, Int], totalCount: Int): (String, Double) = {

    val label2prob = labelTally.keys.map(lv => {

      val logP = featureNames
        .zip(extractFeatures(datum))
        .map({ case (f, fv) => math.log(featureTally((lv, f, fv)) / labelTally(lv)) })
        .reduce(_ + _)

      (lv, (labelTally(lv).toDouble / totalCount) * math.exp(logP))
    })

    println(label2prob)

    label2prob.maxBy(_._2)
  }

  def test(data: Seq[T], featureP: Map[(String, String, String), Int], labelP: Map[String, Int], totalCount: Int): Unit = {
    for (datum <- data) {
      println("classifier: " + classify(datum, featureP, labelP, totalCount) + " given " + extractLabel(datum))
    }
  }

}
