package axle.ml

import cats.Functor
import cats.kernel.Eq
import cats.kernel.Order
import cats.implicits._

import spire.algebra.Field
import spire.implicits.MapInnerProductSpace
// import spire.implicits.StringOrder
import spire.implicits.additiveSemigroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.implicits.eqOps

import axle.algebra._
import axle.math._
import axle.stats.Variable
import axle.stats.TallyDistribution0
import axle.stats.TallyDistribution1
import axle.stats.ProbabilityModel
import axle.syntax.aggregatable._
import axle.syntax.talliable._

case class NaiveBayesClassifier[DATA, FEATURE: Order, CLASS: Order: Eq, F[_], N: Field: Order](
  data:                      F[DATA],
  featureVariablesAndValues: List[(Variable[FEATURE], IndexedSeq[FEATURE])],
  classVariableAndValues:    (Variable[CLASS], IndexedSeq[CLASS]),
  featureExtractor:          DATA => List[FEATURE],
  classExtractor:            DATA => CLASS)(
  implicit
  agg:     Aggregatable[F],
  functor: Functor[F],
  tal:     Talliable[F])
  extends Function1[DATA, CLASS] {

  val featureVariables = featureVariablesAndValues map { _._1 }
  val featureNames = featureVariables map { _.name }

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

  val C = TallyDistribution0(classTally, Variable[CLASS]("class"))

  val probTally0 = implicitly[ProbabilityModel[({ type λ[T] = TallyDistribution0[T, N] })#λ, N]]
  // TODO val probTally1 = implicitly[Probability[({ type λ[T] = TallyDistribution1[T, CLASS, N] })#λ, N]]

  def tallyFor(featureVariable: Variable[FEATURE]): Map[(FEATURE, CLASS), N] =
    featureTally.filter {
      case (k, v) => k._2 === featureVariable.name
    }.map {
      case (k, v) => ((k._3, k._1), v)
    }.withDefaultValue(Field[N].zero)

  // Note: The "parent" (or "given") of these feature variables is C
  val Fs = featureVariablesAndValues.map {
    case (featureVariable, _) =>
      TallyDistribution1(tallyFor(featureVariable).withDefaultValue(Field[N].zero), featureVariable)
  }

  def classes: IndexedSeq[CLASS] = classTally.keySet.toVector.sorted

  def apply(d: DATA): CLASS = {

    val fs = featureExtractor(d)

    def f(c: CLASS): N =
      Π(featureVariables.zip(fs).zip(Fs).map({
        case ((featureVariable, featureValue), featureGivenModel) => {
          // TODO val featureModel = probTally1.condition(featureGivenModel, CaseIs(c, classVariable))
          // TODO probTally.probabilityOf(featureModel, featureValue)
          Field[N].zero
        }
      }))

    def g(c: CLASS): N = probTally0.probabilityOf(C, c) * f(c)

    argmax(C.values, g).get // TODO: will be None if C.values is empty
  }

}
