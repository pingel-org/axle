package axle.ml

import scala.BigDecimal

import axle.enrichGenSeq
import axle.stats.P
import axle.stats.Distribution
import axle.stats.Distribution0
import axle.stats.Distribution1
import axle.stats.TallyDistribution0
import axle.stats.TallyDistribution1
import axle.Π
import spire.algebra.Eq
import spire.algebra.Order
import spire.compat.ordering
import spire.implicits.BigDecimalAlgebra
import spire.implicits.MapInnerProductSpace
import spire.implicits.StringOrder
import spire.implicits.additiveSemigroupOps
import spire.implicits.eqOps
import spire.math.Real
import spire.math.Real.apply

object NaiveBayesClassifier {

  def apply[DATA, FEATURE: Order, CLASS: Order: Eq](
    data: collection.GenSeq[DATA],
    pFs: List[Distribution[FEATURE, BigDecimal]],
    pC: Distribution[CLASS, BigDecimal],
    featureExtractor: DATA => List[FEATURE],
    classExtractor: DATA => CLASS): NaiveBayesClassifier[DATA, FEATURE, CLASS] =
    new NaiveBayesClassifier(data, pFs, pC, featureExtractor, classExtractor)

}

class NaiveBayesClassifier[DATA, FEATURE: Order, CLASS: Order: Eq](
  data: collection.GenSeq[DATA],
  featureRandomVariables: List[Distribution[FEATURE, BigDecimal]],
  classRandomVariable: Distribution[CLASS, BigDecimal],
  featureExtractor: DATA => List[FEATURE],
  classExtractor: DATA => CLASS) extends Classifier[DATA, CLASS]() {

  import axle._

  val featureNames = featureRandomVariables.map(_.name)

  val numFeatures = featureNames.size

  def argmax[K, N: Order](ks: IndexedSeq[K], f: K => N): K = ks.map(k => (k, f(k))).maxBy(_._2)._1

  // TODO no probability should ever be 0

  val emptyFeatureTally = Map.empty[(CLASS, String, FEATURE), BigDecimal].withDefaultValue(BigDecimal(0))

  val featureTally: Map[(CLASS, String, FEATURE), BigDecimal] =
    data.aggregate(emptyFeatureTally)(
      (acc, d) => {
        val fs = featureExtractor(d)
        val c = classExtractor(d)
        val dContrib = featureNames.zip(fs).map({ case (fName, fVal) => ((c, fName, fVal) -> BigDecimal(1)) }).toMap
        acc + dContrib
      },
      _ + _)

  val classTally: Map[CLASS, BigDecimal] = data.map(classExtractor).tally[BigDecimal]

  val C = new TallyDistribution0(classTally, classRandomVariable.name)

  def tallyFor(featureRandomVariable: Distribution[FEATURE, BigDecimal]): Map[(FEATURE, CLASS), BigDecimal] =
    featureTally.filter {
      case (k, v) => k._2 === featureRandomVariable.name
    }.map {
      case (k, v) => ((k._3, k._1), v)
    }.withDefaultValue(BigDecimal(0))

  // Note: The "parent" (or "gien") of these feature variables is C
  val Fs = featureRandomVariables.map(featureRandomVariable =>
    new TallyDistribution1(tallyFor(featureRandomVariable), featureRandomVariable.name))

  def classes: IndexedSeq[CLASS] = classTally.keySet.toVector.sorted

  def apply(d: DATA): CLASS = {
      
    val fs = featureExtractor(d)

    argmax(C.values,
      (c: CLASS) => (P(C is c).apply() *
        ((c: CLASS) => Π(0 until numFeatures)(i => P((Fs(i) is fs(i)) | (C is c)).apply()))(c)))
  }

}
