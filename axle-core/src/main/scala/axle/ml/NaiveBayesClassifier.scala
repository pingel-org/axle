package axle.ml

import axle.enrichGenSeq
import axle.stats.P
import axle.stats.Distribution
import axle.stats.Distribution0
import axle.stats.Distribution1
import axle.stats.TallyDistribution0
import axle.stats.TallyDistribution1
import spire.optional.unicode.Π
import spire.algebra.Eq
import spire.algebra.Order
import spire.algebra.Field
import spire.compat.ordering
import spire.implicits.MapInnerProductSpace
import spire.implicits.StringOrder
import spire.implicits.additiveSemigroupOps
import spire.implicits.eqOps
import spire.math.Real
import spire.math.Real.apply
import spire.implicits._
import spire.syntax._
import axle.stats.P

object NaiveBayesClassifier {

  def apply[DATA, FEATURE: Order, CLASS: Order: Eq, N: Field: Order](
    data: collection.GenSeq[DATA],
    pFs: List[Distribution[FEATURE, N]],
    pC: Distribution[CLASS, N],
    featureExtractor: DATA => List[FEATURE],
    classExtractor: DATA => CLASS): NaiveBayesClassifier[DATA, FEATURE, CLASS, N] =
    new NaiveBayesClassifier(data, pFs, pC, featureExtractor, classExtractor)

}

class NaiveBayesClassifier[DATA, FEATURE: Order, CLASS: Order: Eq, N: Field: Order](
  data: collection.GenSeq[DATA],
  featureRandomVariables: List[Distribution[FEATURE, N]],
  classRandomVariable: Distribution[CLASS, N],
  featureExtractor: DATA => List[FEATURE],
  classExtractor: DATA => CLASS) extends Classifier[DATA, CLASS]() {

  import axle._

  val featureNames = featureRandomVariables map { _.name }

  val numFeatures = featureNames.size

  // TODO no probability should ever be 0

  val emptyFeatureTally = Map.empty[(CLASS, String, FEATURE), N].withDefaultValue(implicitly[Field[N]].zero)

  val featureTally: Map[(CLASS, String, FEATURE), N] =
    data.aggregate(emptyFeatureTally)(
      (acc, d) => {
        val fs = featureExtractor(d)
        val c = classExtractor(d)
        val dContrib = featureNames.zip(fs).map({ case (fName, fVal) => ((c, fName, fVal) -> implicitly[Field[N]].one) }).toMap
        acc + dContrib
      },
      _ + _)

  val classTally: Map[CLASS, N] = data.map(classExtractor).tally[N]

  val C = new TallyDistribution0(classTally, classRandomVariable.name)

  def tallyFor(featureRandomVariable: Distribution[FEATURE, N]): Map[(FEATURE, CLASS), N] =
    featureTally.filter {
      case (k, v) => k._2 === featureRandomVariable.name
    }.map {
      case (k, v) => ((k._3, k._1), v)
    }.withDefaultValue(implicitly[Field[N]].zero)

  // Note: The "parent" (or "gien") of these feature variables is C
  val Fs = featureRandomVariables.map(featureRandomVariable =>
    new TallyDistribution1(tallyFor(featureRandomVariable), featureRandomVariable.name))

  def classes: IndexedSeq[CLASS] = classTally.keySet.toVector.sorted

  def apply(d: DATA): CLASS = {

    val fs = featureExtractor(d)

    argmax(C.values,
      (c: CLASS) => (P(C is c).apply() *
        ((c: CLASS) => Π((0 until numFeatures) map { i => P((Fs(i) is fs(i)) | (C is c)).apply() }))(c)))
  }

}
