package axle.ml

import axle.matrix._
import axle.matrix.JblasMatrixModule._

class ConfusionMatrix[T, C: Ordering, L: Ordering](classifier: Classifier[T, C], data: Seq[T], labelExtractor: T => L) {

  import math.{ ceil, log10 }

  val label2clusterId = data.map(datum => (labelExtractor(datum), classifier(datum)))

  val labelList = label2clusterId.map(_._1).toSet.toList
  val labelIndices = labelList.zipWithIndex.toMap

  val labelIdClusterId2count = label2clusterId
    .map({ case (label, clusterId) => ((labelIndices(label), clusterId), 1) })
    .groupBy(_._1)
    .map({ case (k, v) => (k, v.map(_._2).sum) })
    .withDefaultValue(0)

  val classes = classifier.classes

  val counts = matrix[Int](labelList.length, classes.size, (r: Int, c: Int) => labelIdClusterId2count((r, classes(c))))

  val width = ceil(log10(data.length)).toInt

  val formatNumber = (i: Int) => ("%" + width + "d").format(i)

  lazy val rowSums = counts.rowSums()
  lazy val columnSums = counts.columnSums()

  lazy val asString = (labelList.zipWithIndex.map({
    case (label, r) => ((0 until counts.columns).map(c => formatNumber(counts(r, c))).mkString(" ") + " : " + formatNumber(rowSums(r, 0)) + " " + label + "\n")
  }).mkString("")) + "\n" +
    (0 until counts.columns).map(c => formatNumber(columnSums(0, c))).mkString(" ") + "\n"

  override def toString: String = asString
}

