package axle.ml

import axle.stats._

object NaiveBayesClassifier {

  def apply[D, TF, TC](data: Seq[D], pFs: List[RandomVariable[TF]], pC: RandomVariable[TC], featureExtractor: D => List[TF], classExtractor: D => TC) =
    new NaiveBayesClassifier(data, pFs, pC, featureExtractor, classExtractor)

}

class NaiveBayesClassifier[D, TF, TC](
  data: Seq[D],
  pFs: List[RandomVariable[TF]],
  pC: RandomVariable[TC],
  featureExtractor: D => List[TF],
  classExtractor: D => TC) extends Classifier(classExtractor) {

  import axle.ScalaMapReduce._
  import axle._
  import collection._

  val featureNames = pFs.map(_.name)

  val N = featureNames.size

  def argmax[K](ks: Iterable[K], f: K => Double): K = ks.map(k => (k, f(k))).maxBy(_._2)._1

  // TODO no probability should ever be 0

  val featureTally: immutable.Map[(TC, String, TF), Int] = mapReduce(data.iterator,
    mapper = (d: D) => {
      val fs = featureExtractor(d)
      (0 until fs.length).map(i => ((classExtractor(d), featureNames(i), fs(i)), 1))
    },
    reducer = (x: Int, y: Int) => x + y
  ).withDefaultValue(0)

  val classTally: immutable.Map[TC, Int] = mapReduce(data.iterator,
    mapper = (d: D) => List((classExtractor(d), 1)),
    reducer = (x: Int, y: Int) => x + y
  ).withDefaultValue(1) // to avoid division by zero

  val C = new RandomVariable0(pC.name, pC.values,
    distribution = Some(new TallyDistribution0(classTally)))

  val Fs = pFs.map(pF => new RandomVariable1(
    pF.name,
    pF.values,
    grv = C,
    distribution = Some(new TallyDistribution1(
      featureTally
        .filter(_._1._2 == pF.name)
        .map(kv => ((kv._1._3, kv._1._1), kv._2))
        .withDefaultValue(0)))))

  def predict(d: D): TC = {
    val fs = featureExtractor(d)
    argmax(C, (c: TC) => P(C is c) * (0 until N).Π(i => P((Fs(i) is fs(i)) | (C is c))))
  }

}
