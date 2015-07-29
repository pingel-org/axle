package axle.ml

import axle.algebra.argmax
import axle.algebra.Functor
import axle.algebra.Reducible
import axle.enrichGenSeq
import axle.stats.P
import axle.stats.Distribution
import axle.stats.Distribution0
import axle.stats.Distribution1
import axle.stats.TallyDistribution0
import axle.stats.TallyDistribution1
import axle.syntax.aggregatable._
import axle.syntax.functor._
import spire.algebra.Eq
import spire.algebra.Order
import spire.algebra.Field
import spire.compat.ordering
import spire.implicits.MapInnerProductSpace
import spire.implicits.StringOrder
import spire.implicits.additiveSemigroupOps
import spire.implicits.eqOps
import spire.math.Rational
import spire.math.Real
import spire.math.Real.apply
import spire.math._
import spire.implicits._
import spire.syntax._
import axle.stats.P
import axle.algebra._

case class NaiveBayesClassifier[DATA, FEATURE: Order, CLASS: Order: Eq, F](
  data: F[DATA],
  featureRandomVariables: List[Distribution[FEATURE, Rational]],
  classRandomVariable: Distribution[CLASS, Rational],
  featureExtractor: DATA => List[FEATURE],
  classExtractor: DATA => CLASS)(
    implicit agg: Aggregatable[F],
    functor: Functor[F])
    extends Function1[DATA, CLASS] {

  import axle._

  val featureNames = featureRandomVariables map { _.name }

  val numFeatures = featureNames.size

  // TODO no probability should ever be 0

  val emptyFeatureTally = Map.empty[(CLASS, String, FEATURE), Rational].withDefaultValue(Field[Rational].zero)

  val featureTally: Map[(CLASS, String, FEATURE), Rational] =
    data.aggregate(emptyFeatureTally)(
      (acc, d) => {
        val fs = featureExtractor(d)
        val c = classExtractor(d)
        val dContrib = featureNames.zip(fs).map({ case (fName, fVal) => ((c, fName, fVal) -> Field[Rational].one) }).toMap
        acc + dContrib
      },
      _ + _)

  val classTally: Map[CLASS, Rational] =
    data.map(classExtractor).tally[Rational].withDefaultValue(Field[Rational].zero)

  val C = TallyDistribution0(classTally, classRandomVariable.name)

  def tallyFor(featureRandomVariable: Distribution[FEATURE, Rational]): Map[(FEATURE, CLASS), Rational] =
    featureTally.filter {
      case (k, v) => k._2 === featureRandomVariable.name
    }.map {
      case (k, v) => ((k._3, k._1), v)
    }.withDefaultValue(Field[Rational].zero)

  // Note: The "parent" (or "given") of these feature variables is C
  val Fs = featureRandomVariables.map(featureRandomVariable =>
    TallyDistribution1(
      tallyFor(featureRandomVariable).withDefaultValue(Field[Rational].zero),
      featureRandomVariable.name))

  def classes: IndexedSeq[CLASS] = classTally.keySet.toVector.sorted

  def apply(d: DATA): CLASS = {

    val fs = featureExtractor(d)

    def f(c: CLASS): Rational =
      Π(Fs.zip(fs).map({
        case (fVar, fVal) => {
          P((fVar is fVal) | (C is c)).apply()
        }
      }))

    def g(c: CLASS) = P(C is c).apply() * f(c)

    argmax(C.values, g).get // TODO: will be None if C.values is empty
  }

}