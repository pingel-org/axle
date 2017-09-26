package axle.ml

import cats.kernel.Eq
import cats.kernel.Order
import cats.Order.catsKernelOrderingForOrder
import spire.algebra.Field
import spire.implicits.MapInnerProductSpace
import spire.implicits.StringOrder
import spire.implicits.additiveSemigroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.implicits.eqOps
import axle.stats.Variable
import axle.stats.TallyDistribution0
import axle.stats.TallyDistribution1
import axle.syntax.aggregatable._
import axle.syntax.functor._
import axle.syntax.talliable._
import axle.stats.P
import axle.algebra._
import axle.math._

case class NaiveBayesClassifier[DATA, FEATURE: Order, CLASS: Order: Eq, F, G, N: Field: Order](
  data: F,
  featureRandomVariables: List[Variable[FEATURE]],
  classRandomVariable: Variable[CLASS],
  featureExtractor: DATA => List[FEATURE],
  classExtractor: DATA => CLASS)(
    implicit agg: Aggregatable[F, DATA, Map[(CLASS, String, FEATURE), N]],
    functor: Functor[F, DATA, CLASS, G],
    tal: Talliable[G, CLASS, N])
    extends Function1[DATA, CLASS] {

  val featureNames = featureRandomVariables map { _.name }

  val numFeatures = featureNames.size

  // TODO no probability should ever be 0

  val emptyFeatureTally = Map.empty[(CLASS, String, FEATURE), N].withDefaultValue(Field[N].zero)

  val featureTally: Map[(CLASS, String, FEATURE), N] =
    data.aggregate(emptyFeatureTally)(
      (acc, d) => {
        val fs = featureExtractor(d)
        val c = classExtractor(d)
        val dContrib = featureNames.zip(fs).map({ case (fName, fVal) => ((c, fName, fVal) -> Field[N].one) }).toMap
        acc + dContrib
      },
      _ + _)

  val classTally: Map[CLASS, N] =
    data.map(classExtractor).tally.withDefaultValue(Field[N].zero)

  val C = TallyDistribution0(classTally, classRandomVariable.name)

  def tallyFor(featureRandomVariable: Variable[FEATURE]): Map[(FEATURE, CLASS), N] =
    featureTally.filter {
      case (k, v) => k._2 === featureRandomVariable.name
    }.map {
      case (k, v) => ((k._3, k._1), v)
    }.withDefaultValue(Field[N].zero)

  // Note: The "parent" (or "given") of these feature variables is C
  val Fs = featureRandomVariables.map(featureRandomVariable =>
    TallyDistribution1(
      tallyFor(featureRandomVariable).withDefaultValue(Field[N].zero),
      featureRandomVariable.name))

  def classes: IndexedSeq[CLASS] = classTally.keySet.toVector.sorted

  def apply(d: DATA): CLASS = {

    val fs = featureExtractor(d)

    def f(c: CLASS): N =
      Π(Fs.zip(fs).map({
        case (fVar, fVal) => {
          P((fVar is fVal) | (C is c)).apply()
        }
      }))

    def g(c: CLASS) = P(C is c).apply() * f(c)

    argmax(C.values, g).get // TODO: will be None if C.values is empty
  }

}

object NaiveBayesClassifier {

  def common[DATA, FEATURE: Order, CLASS: Order: Eq, U[_], N: Field: Order](
    data: U[DATA],
    featureRandomVariables: List[Variable[FEATURE]],
    classRandomVariable: Variable[CLASS],
    featureExtractor: DATA => List[FEATURE],
    classExtractor: DATA => CLASS)(
      implicit agg: Aggregatable[U[DATA], DATA, Map[(CLASS, String, FEATURE), N]],
      functor: Functor[U[DATA], DATA, CLASS, U[CLASS]],
      tal: Talliable[U[CLASS], CLASS, N]) =
    NaiveBayesClassifier(data, featureRandomVariables, classRandomVariable, featureExtractor, classExtractor)
}
