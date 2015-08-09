package axle.ml

import axle.Show
import axle.algebra.LinearAlgebra
import axle.algebra.Functor
import axle.algebra.Finite
import axle.algebra.SetFrom
import axle.algebra.MapReducible
import axle.algebra.MapFrom
import axle.algebra.FunctionPair
import axle.syntax.linearalgebra._
import axle.syntax.functor._
import axle.syntax.finite._
import axle.syntax.mapfrom._
import axle.syntax.aggregatable._
import axle.syntax.mapreducible._
import axle.syntax.setfrom._
import spire.algebra._

import math.{ ceil, log10 }

case class ConfusionMatrix[T, CLASS: Order, L: Order, F, M, G, H](
    classifier: Function1[T, CLASS],
    data: F,
    labelExtractor: T => L,
    classes: IndexedSeq[CLASS])(
        implicit val la: LinearAlgebra[M, Int, Int, Double],
        finite: Finite[F, Int],
        functorF: Functor[F, T, (L, CLASS), G],
        functorG: Functor[G, (L, CLASS), L, H],
        sf: SetFrom[H, L],
        mr: MapReducible[G, (L, CLASS), Int, (Int, CLASS), Map[(Int, CLASS), Int]],
        mf: MapFrom[List[(L, Int)], L, Int]) {

  val label2clusterId = data.map(datum => (labelExtractor(datum), classifier(datum)))

  val labelList = label2clusterId.map(_._1).toSet.toList

  val labelIndices = labelList.zipWithIndex.toMap

  val labelIdClusterId2count =
    label2clusterId.mapReduce( // [Int, (Int, CLASS)]
      (lc: (L, CLASS)) => ((labelIndices(lc._1), lc._2), 1),
      0,
      _ + _).toMap.withDefaultValue(0)

  val counts = la.matrix(
    labelList.length,
    classes.size,
    (r: Int, c: Int) => labelIdClusterId2count((r, classes(c))))

  val width = ceil(log10(data.size)).toInt

  val formatNumber = (n: Int) => ("%" + width + "d").format(n)

  lazy val rowSums = counts.rowSums
  lazy val columnSums = counts.columnSums

}

object ConfusionMatrix {

  implicit def showCM[T, CLASS, L, F, M, G, H](implicit la: LinearAlgebra[M, Int, Int, Double]): Show[ConfusionMatrix[T, CLASS, L, F, M, G, H]] =
    new Show[ConfusionMatrix[T, CLASS, L, F, M, G, H]] {

      def text(cm: ConfusionMatrix[T, CLASS, L, F, M, G, H]): String = {
        (cm.labelList.zipWithIndex.map({
          case (label, r) => ((0 until cm.counts.columns).map(c => cm.formatNumber(cm.counts.get(r, c).toInt)).mkString(" ") + " : " + cm.formatNumber(cm.rowSums.get(r, 0).toInt) + " " + label + "\n")
        }).mkString("")) + "\n" +
          (0 until cm.counts.columns).map(c => cm.formatNumber(cm.columnSums.get(0, c).toInt)).mkString(" ") + "\n"
      }

    }

}
