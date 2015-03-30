package axle.algebra

import axle.enrichGenTraversable
import spire.algebra.MetricSpace
import scala.reflect.ClassTag

// http://en.wikipedia.org/wiki/Triangle_inequality
// def triangleInequalityHolds(data: collection.GenTraversable[T]): Boolean =
//   data.triples.forall({ case (a, b, c) => distance(a, b) + distance(b, c) >= distance(a, c) })

case class DistanceMatrix[T: ClassTag, F[_], M](
  vectors: F[T])(
    implicit space: MetricSpace[T, Double],
    la: LinearAlgebra[M, Int, Int, Double],
    index: Indexed[F, Int],
    finite: Finite[F, Int]) {

  val n = finite.size(vectors)

  val distanceMatrix: M =
    la.matrix(n, n, (r: Int, c: Int) => space.distance(index.at(vectors)(r), index.at(vectors)(c)).toDouble)

  //  def nMostSimilar(query: T, vectors: Iterator[T], c: Int): List[(Int, Double)] =
  //    vectors.zipWithIndex
  //      .map({ case (v, i) => (i, distance(query, v)) })
  //      .toList
  //      .sortBy(_._2)
  //      .take(c)

}
