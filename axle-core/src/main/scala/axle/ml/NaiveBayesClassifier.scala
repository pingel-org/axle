package axle.ml

import axle._
import axle.stats._
import spire.implicits._
import spire.algebra._
import spire.compat._

object NaiveBayesClassifier {

  def apply[DATA, FEATURE, CLASS: Ordering: Eq](
    data: collection.GenSeq[DATA],
    pFs: List[RandomVariable[FEATURE]],
    pC: RandomVariable[CLASS],
    featureExtractor: DATA => List[FEATURE],
    classExtractor: DATA => CLASS): NaiveBayesClassifier[DATA, FEATURE, CLASS] =
    new NaiveBayesClassifier(data, pFs, pC, featureExtractor, classExtractor)

}

class NaiveBayesClassifier[DATA, FEATURE, CLASS: Ordering: Eq](
  data: collection.GenSeq[DATA],
  featureRandomVariables: List[RandomVariable[FEATURE]],
  classRandomVariable: RandomVariable[CLASS],
  featureExtractor: DATA => List[FEATURE],
  classExtractor: DATA => CLASS) extends Classifier[DATA, CLASS]() {

  import axle._

  val featureNames = featureRandomVariables.map(_.name)

  val N = featureNames.size

  def argmax[K, N: Ordering](ks: IndexedSeq[K], f: K => N): K = ks.map(k => (k, f(k))).maxBy(_._2)._1

  // TODO no probability should ever be 0

  val emptyFeatureTally = Map.empty[(CLASS, String, FEATURE), Long].withDefaultValue(0L)

  val featureTally: Map[(CLASS, String, FEATURE), Long] =
    data.aggregate(emptyFeatureTally)(
      (acc, d) => {
        val fs = featureExtractor(d)
        val c = classExtractor(d)
        val dContrib = featureNames.zip(fs).map({ case (fName, fVal) => ((c, fName, fVal) -> 1L) }).toMap
        acc + dContrib
      },
      _ + _)

  val classTally: Map[CLASS, Long] = data.map(d => classExtractor(d)).tally

  val C = new RandomVariable0(classRandomVariable.name, classRandomVariable.values,
    distribution = Some(new TallyDistribution0(classTally)))

  val Fs = featureRandomVariables.map(featureRandomVariable => new RandomVariable1(
    featureRandomVariable.name,
    featureRandomVariable.values,
    grv = C,
    distribution = Some(new TallyDistribution1(
      featureTally.filter {
        case (k, v) => k._2 == featureRandomVariable.name
      }.map {
        case (k, v) => ((k._3, k._1), v)
      }.withDefaultValue(0L)))))

  def classes: IndexedSeq[CLASS] = classTally.keySet.toVector.sorted

  def apply(d: DATA): CLASS = {
    val fs = featureExtractor(d)
    argmax(C, (c: CLASS) => (P(C is c) * (0 until N).toVector.Π(i => P((Fs(i) is fs(i)) | (C is c))))())
  }

}
