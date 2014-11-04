package axle.ml

import axle.matrix._
import axle.matrix.MatrixModule
import spire.algebra._
import axle.algebra._
import scala.reflect.ClassTag
import math.{ ceil, log10 }

abstract class ConfusionMatrix[T: ClassTag, CLASS: Order, L: Order: ClassTag, F[_]: Functor: Finite: SetFrom: MapReducible: MapFrom](
  classifier: Classifier[T, CLASS],
  data: F[T],
  labelExtractor: T => L)
  extends MatrixModule {

  val func = implicitly[Functor[F]]
  val sz = implicitly[Finite[F]]
  val settable = implicitly[SetFrom[F]]
  val mr = implicitly[MapReducible[F]]
  val mf = implicitly[MapFrom[F]]

  val label2clusterId = func.map(data)(datum => (labelExtractor(datum), classifier(datum)))

  val labelList: List[L] = settable.toSet(func.map(label2clusterId)(_._1)).toList
  val labelIndices: Map[L, Int] = labelList.zipWithIndex.toMap

  val labelIdClusterId2count =
    mf.toMap(
      mr.mapReduce[(L, CLASS), Int, (Int, CLASS)](
        label2clusterId,
        (lc: (L, CLASS)) => ((labelIndices(lc._1), lc._2), 1),
        0,
        _ + _)).withDefaultValue(0)

  val classes = classifier.classes

  val counts = matrix[Int](
    labelList.length,
    classes.size,
    (r: Int, c: Int) => labelIdClusterId2count((r, classes(c))))

  val width = ceil(log10(sz.size(data))).toInt

  val formatNumber = (i: Int) => ("%" + width + "d").format(i)

  lazy val rowSums = counts.rowSums
  lazy val columnSums = counts.columnSums

  lazy val asString = (labelList.zipWithIndex.map({
    case (label, r) => ((0 until counts.columns).map(c => formatNumber(counts(r, c))).mkString(" ") + " : " + formatNumber(rowSums(r, 0)) + " " + label + "\n")
  }).mkString("")) + "\n" +
    (0 until counts.columns).map(c => formatNumber(columnSums(0, c))).mkString(" ") + "\n"

  override def toString: String = asString
}
