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
import axle.syntax.mapreducible._
import axle.syntax.setfrom._
import spire.algebra._
import scala.reflect.ClassTag
import math.{ ceil, log10 }

case class ConfusionMatrix[T: ClassTag, CLASS: Order, L: Order: ClassTag, F[_]: Functor: Finite: SetFrom: MapReducible: MapFrom, M](
  classifier: Classifier[T, CLASS],
  data: F[T],
  labelExtractor: T => L)(implicit val la: LinearAlgebra[M, Double]) {

  val label2clusterId = data.map(datum => (labelExtractor(datum), classifier(datum)))

  val labelList = label2clusterId.map(_._1).toSet.toList

  val labelIndices = labelList.zipWithIndex.toMap

  val labelIdClusterId2count =
    label2clusterId.mapReduce[Int, (Int, CLASS)](
      (lc: (L, CLASS)) => ((labelIndices(lc._1), lc._2), 1),
      0,
      _ + _).toMap.withDefaultValue(0)

  val classes = classifier.classes

  val counts = la.matrix(
    labelList.length,
    classes.size,
    (r: Int, c: Int) => labelIdClusterId2count((r, classes(c))))

  val width = ceil(log10(data.size)).toInt

  val formatNumber = (n: Double) => ("%" + width + "d").format(n)

  lazy val rowSums = counts.rowSums
  lazy val columnSums = counts.columnSums

}

object ConfusionMatrix {

  implicit def showCM[T, CLASS, L, F[_], M](implicit la: LinearAlgebra[M, Double]): Show[ConfusionMatrix[T, CLASS, L, F, M]] =
    new Show[ConfusionMatrix[T, CLASS, L, F, M]] {

      def text(cm: ConfusionMatrix[T, CLASS, L, F, M]): String = {
        (cm.labelList.zipWithIndex.map({
          case (label, r) => ((0 until cm.counts.columns).map(c => cm.formatNumber(cm.counts.get(r, c))).mkString(" ") + " : " + cm.formatNumber(cm.rowSums.get(r, 0)) + " " + label + "\n")
        }).mkString("")) + "\n" +
          (0 until cm.counts.columns).map(c => cm.formatNumber(cm.columnSums.get(0, c))).mkString(" ") + "\n"
      }

    }

}
