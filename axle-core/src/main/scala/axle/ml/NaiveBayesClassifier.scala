package axle.ml

import axle.stats._
import spire.implicits._
import spire.algebra._

object NaiveBayesClassifier {

  def apply[DATA, FEATURE, CLASS: Ordering](data: Seq[DATA], pFs: List[RandomVariable[FEATURE]], pC: RandomVariable[CLASS], featureExtractor: DATA => List[FEATURE], classExtractor: DATA => CLASS) =
    new NaiveBayesClassifier(data, pFs, pC, featureExtractor, classExtractor)

}

class NaiveBayesClassifier[DATA, FEATURE, CLASS: Ordering](
  data: Seq[DATA],
  featureRandomVariables: List[RandomVariable[FEATURE]],
  classRandomVariable: RandomVariable[CLASS],
  featureExtractor: DATA => List[FEATURE],
  classExtractor: DATA => CLASS) extends Classifier[DATA, CLASS]() {

  import axle._

  val featureNames = featureRandomVariables.map(_.name)

  val N = featureNames.size

  def argmax[K](ks: IndexedSeq[K], f: K => Double): K = ks.map(k => (k, f(k))).maxBy(_._2)._1

  // TODO no probability should ever be 0

  implicit val intsemi = axle.algebra.Semigroups.IntSemigroup // TODO remove this

  val featureTally =
    data.aggregate(Map.empty[(CLASS, String, FEATURE), Int].withDefaultValue(0))(
      (tally, d) => {
        val fs = featureExtractor(d)
        val c = classExtractor(d)
        tally |+| featureNames.zip(fs).map({ case (fName, fVal) => ((c, fName, fVal) -> 1) }).toMap
      },
      _ |+| _
    )

  val classTally =
    data.aggregate(Map.empty[CLASS, Int].withDefaultValue(0))(
      (tally, d) => tally + (classExtractor(d) -> 1),
      _ |+| _
    ).withDefaultValue(1) // to avoid division by zero

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
      }.withDefaultValue(0))
    )))

  def classes(): IndexedSeq[CLASS] = classTally.keySet.toVector.sorted

  def apply(d: DATA): CLASS = {
    val fs = featureExtractor(d)
    argmax(C, (c: CLASS) => P(C is c) * (0 until N).Π(i => P((Fs(i) is fs(i)) | (C is c))))
  }

}
