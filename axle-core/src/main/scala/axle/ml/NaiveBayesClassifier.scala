package axle.ml

import axle.stats._

object NaiveBayesClassifier {

  def apply[DATA, FEATURE, CLASS](data: Seq[DATA], pFs: List[RandomVariable[FEATURE]], pC: RandomVariable[CLASS], featureExtractor: DATA => List[FEATURE], classExtractor: DATA => CLASS) =
    new NaiveBayesClassifier(data, pFs, pC, featureExtractor, classExtractor)

}

class NaiveBayesClassifier[DATA, FEATURE, CLASS](
  data: Seq[DATA],
  featureRandomVariables: List[RandomVariable[FEATURE]],
  classRandomVariable: RandomVariable[CLASS],
  featureExtractor: DATA => List[FEATURE],
  classExtractor: DATA => CLASS) extends Classifier(classExtractor) {

  import axle._
  import collection._

  val featureNames = featureRandomVariables.map(_.name)

  val N = featureNames.size

  def argmax[K](ks: IndexedSeq[K], f: K => Double): K = ks.map(k => (k, f(k))).maxBy(_._2)._1

  // TODO no probability should ever be 0

  // TODO: rephrase tallies as aggregations (vs. folds)

  val featureTally =
    data.foldLeft(immutable.Map.empty[(CLASS, String, FEATURE), Int].withDefaultValue(0))({
      case (tally, d) => {
        val fs = featureExtractor(d)
        val c = classExtractor(d)
        (0 until fs.length).foldLeft(tally)({
          case (tally, i) => {
            val k = (c, featureNames(i), fs(i))
            tally + (k -> (tally(k) + 1))
          }
        })
      }
    })

  val classTally =
    data.foldLeft(immutable.Map.empty[CLASS, Int].withDefaultValue(0))({
      case (tally, d) => {
        val c = classExtractor(d)
        tally + (c -> (tally(c) + 1))
      }
    }).withDefaultValue(1) // to avoid division by zero

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

  def apply(d: DATA): CLASS = {
    val fs = featureExtractor(d)
    argmax(C, (c: CLASS) => P(C is c) * (0 until N).Π(i => P((Fs(i) is fs(i)) | (C is c))))
  }

}
