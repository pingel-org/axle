package axle.ml

import scala.math.min

import axle.algebra.mean
import axle.algebra.Σ
import spire.implicits.DoubleAlgebra

object RankedClassifierPerformance {

  /**
   * http://en.wikipedia.org/wiki/Information_retrieval#Average_precision
   *
   */

  def averagePrecisionAtK[T](actual: Seq[T], predicted: Seq[T], k: Int): Double = {

    if (actual.length == 0) {
      0d
    } else {

      val cutOff = predicted.take(k)

      val score =
        Σ(cutOff
          .zipWithIndex
          .filter({ case (p, i) => actual.contains(p) && (!cutOff.take(i).contains(p)) })
          .zipWithIndex
          .map({ case ((_, i), h) => (h + 1d) / (i + 1d) }))

      score / min(actual.length, k)
    }
  }

  /**
   *
   * https://www.kaggle.com/wiki/MeanAveragePrecision
   *
   * http://www.kaggle.com/c/FacebookRecruiting/forums/t/2002/alternate-explanation-of-mean-average-precision
   *
   */

  def meanAveragePrecisionAtK[T](actual: Seq[Seq[T]], predicted: Seq[Seq[T]], k: Int = 10): Double =
    mean(actual.zip(predicted).map({ case (a, p) => averagePrecisionAtK(a, p, k) }))

}
