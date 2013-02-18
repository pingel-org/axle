package axle.algebra

import spire.math._
import spire.algebra.MetricSpace
import axle._

class EnrichedMetricSpace[T](space: MetricSpace[T, Real]) {

  import axle.matrix.JblasMatrixModule._

  def distanceMatrix(vectors: IndexedSeq[T]): Matrix[Double] = {
    val n = vectors.size
    matrix(n, n, (r: Int, c: Int) => space.distance(vectors(r), vectors(c)).toDouble)
  }

  def nMostSimilar(query: T, vectors: Iterator[T], c: Int): List[(Int, Double)] =
    vectors.zipWithIndex
      .map({ case (v, i) => (i, space.distance(query, v)) })
      .toList
      .sortBy(_._2)
      .take(c)
      .map(d => (d._1, d._2.toDouble))

  //  def nMostSimilarReport(query: String, n: Int) = nMostSimilar(query, n)
  //    .map(is => (is._2, corpus(is._1))).map(sd => "%.4f %s".format(sd._1, sd._2)).mkString("\n")

  def distance(s1: String, s2: String): Int

  /**
   * triangleInequalityHolds
   *
   * Applies the Triangle Inequality using all the triples formed by the
   * given data to see if this is a true "distance"
   *
   * http://en.wikipedia.org/wiki/Triangle_inequality
   */

  def triangleInequalityHolds(data: IndexedSeq[String]): Boolean =
    data.triples.∀({
      case (a, b, c) =>
        distance(a, b) + distance(b, c) >= distance(a, c)
    })

}

