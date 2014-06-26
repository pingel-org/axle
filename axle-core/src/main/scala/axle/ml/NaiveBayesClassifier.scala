package axle.ml

import axle.enrichGenSeq
import axle.stats.P
import axle.stats.RandomVariable
import axle.stats.RandomVariable0
import axle.stats.RandomVariable1
import axle.stats.TallyDistribution0
import axle.stats.TallyDistribution1
import axle.stats.evalProbability
import axle.stats.rv2it
import axle.Π
import spire.algebra.Eq
import spire.algebra.Order
import spire.compat.ordering
import spire.implicits.LongAlgebra
import spire.implicits.MapRng
import spire.implicits.StringOrder
import spire.implicits.additiveSemigroupOps
import spire.implicits.eqOps
import spire.math.Rational

object NaiveBayesClassifier {

  def apply[DATA, FEATURE: Order, CLASS: Order: Eq](
    data: collection.GenSeq[DATA],
    pFs: List[RandomVariable[FEATURE, Rational]],
    pC: RandomVariable[CLASS, Rational],
    featureExtractor: DATA => List[FEATURE],
    classExtractor: DATA => CLASS): NaiveBayesClassifier[DATA, FEATURE, CLASS] =
    new NaiveBayesClassifier(data, pFs, pC, featureExtractor, classExtractor)

}

class NaiveBayesClassifier[DATA, FEATURE: Order, CLASS: Order: Eq](
  data: collection.GenSeq[DATA],
  featureRandomVariables: List[RandomVariable[FEATURE, Rational]],
  classRandomVariable: RandomVariable[CLASS, Rational],
  featureExtractor: DATA => List[FEATURE],
  classExtractor: DATA => CLASS) extends Classifier[DATA, CLASS]() {

  import axle._

  val featureNames = featureRandomVariables.map(_.name)

  val numFeatures = featureNames.size

  def argmax[K, N: Order](ks: IndexedSeq[K], f: K => N): K = ks.map(k => (k, f(k))).maxBy(_._2)._1

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

  val classTally: Map[CLASS, Long] = data.map(classExtractor).tally

  val C = RandomVariable0(classRandomVariable.name, new TallyDistribution0(classTally))

  val Fs = featureRandomVariables.map(featureRandomVariable => RandomVariable1(
    featureRandomVariable.name,
    C,
    new TallyDistribution1(
      featureTally.filter {
        case (k, v) => k._2 === featureRandomVariable.name
      }.map {
        case (k, v) => ((k._3, k._1), v)
      }.withDefaultValue(0L))))

  def classes: IndexedSeq[CLASS] = classTally.keySet.toVector.sorted

  def apply(d: DATA): CLASS = {
    val fs = featureExtractor(d)

    val foo: Int => CLASS => () => Rational = (i: Int) => (c: CLASS) => P((Fs(i) is fs(i)) | (C is c))

    argmax(C,
      (c: CLASS) => (P(C is c) *
        ((c: CLASS) => Π(0 until numFeatures)(i => foo(i)(c)()))(c)))
  }

}
