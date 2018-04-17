package axle.ml

import cats.Functor
import cats.Show
import cats.kernel.Order
import cats.implicits._

import spire.math.ceil
import spire.math.log10

import axle.algebra.LinearAlgebra
import axle.algebra.Finite
import axle.algebra.MapFrom
import axle.algebra.SetFrom
import axle.algebra.MapReducible
import axle.syntax.linearalgebra._
import axle.syntax.finite._
import axle.syntax.mapfrom._
import axle.syntax.mapreducible._
import axle.syntax.setfrom._

case class ConfusionMatrix[T, CLASS: Order, L: Order, F[_], M](
  classifier:     Function1[T, CLASS],
  data:           F[T],
  labelExtractor: T => L,
  classes:        IndexedSeq[CLASS])(
  implicit
  val la:  LinearAlgebra[M, Int, Int, Double],
  finite:  Finite[F, Int],
  functor: Functor[F],
  sf:      SetFrom[F[L], L],
  mf:      MapFrom[F[((Int, CLASS), Int)], (Int, CLASS), Int],
  mr:      MapReducible[F]) {

  val label2clusterId = data.map(datum => (labelExtractor(datum), classifier(datum)))

  val labelList = label2clusterId.map(_._1).toSet.toList

  val labelIndices = labelList.zipWithIndex.toMap

  val labelIdClusterIdCountPair = label2clusterId.mapReduce(
    (lc: (L, CLASS)) => ((labelIndices(lc._1), lc._2), 1),
    0,
    (x: Int, y: Int) => x + y)

  val labelIdClusterId2count = labelIdClusterIdCountPair.toMap.withDefaultValue(0)

  val counts = la.matrix(
    labelList.length,
    classes.size,
    (r: Int, c: Int) => labelIdClusterId2count((r, classes(c))).toDouble)

  val width = ceil(log10(data.size.toDouble)).toInt

  val formatNumber = (n: Int) => ("%" + width + "d").format(n)

  lazy val rowSums = counts.rowSums
  lazy val columnSums = counts.columnSums

}

object ConfusionMatrix {

  implicit def showCM[T, CLASS, L, F[_], M](
    implicit
    la: LinearAlgebra[M, Int, Int, Double]): Show[ConfusionMatrix[T, CLASS, L, F, M]] = cm =>
    (cm.labelList.zipWithIndex.map({
      case (label, r) => ((0 until cm.counts.columns).map(c => cm.formatNumber(cm.counts.get(r, c).toInt)).mkString(" ") + " : " + cm.formatNumber(cm.rowSums.get(r, 0).toInt) + " " + label + "\n")
    }).mkString("")) + "\n" +
      (0 until cm.counts.columns).map(c => cm.formatNumber(cm.columnSums.get(0, c).toInt)).mkString(" ") + "\n"

}
