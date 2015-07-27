package axle.algebra

import scala.reflect.ClassTag
import scala.annotation.implicitNotFound

@implicitNotFound("Witness not found for MapReducible[${M}]")
trait MapReducible[M, A, B, K] {

  def mapReduce(
    input: M,
    mapper: A => (K, B),
    zero: B,
    op: (B, B) => B): M
}

object MapReducible {

  @inline final def apply[M, A, B, K](implicit mra: MapReducible[M, A, B, K]): MapReducible[M, A, B, K] =
    implicitly[MapReducible[M, A, B, K]]

  implicit def mapReduceSeq[A, B, K]: MapReducible[Seq[A], A, B, K] =
    new MapReducible[Seq[A], A, B, K] {

      def mapReduce(
        input: Seq[A],
        mapper: A => (K, B),
        zero: B,
        reduce: (B, B) => B): Seq[(K, B)] =
        input.map(mapper).groupBy(_._1).mapValues(kbs => kbs.map(_._2).foldLeft(zero)(reduce)).toSeq
    }

  implicit def mapReduceVector[A, B, K]: MapReducible[Vector[A], A, B, K] =
    new MapReducible[Vector[A], A, B, K] {

      def mapReduce(
        input: Vector[A],
        mapper: A => (K, B),
        zero: B,
        reduce: (B, B) => B): Vector[(K, B)] =
        input.map(mapper).groupBy(_._1).mapValues(kbs => kbs.map(_._2).foldLeft(zero)(reduce)).toVector
    }

  implicit def mapReduceList[A, B, K]: MapReducible[List[A], A, B, K] =
    new MapReducible[List[A], A, B, K] {

      def mapReduce(
        input: List[A],
        mapper: A => (K, B),
        zero: B,
        reduce: (B, B) => B): List[(K, B)] =
        input.map(mapper).groupBy(_._1).mapValues(kbs => kbs.map(_._2).foldLeft(zero)(reduce)).toList
    }

}
