package axle.algebra

import scala.annotation.implicitNotFound

@implicitNotFound("Witness not found for MapReducible[${M}, ${A}, ${B}, ${K}, ${G}]")
trait MapReducible[M, A, B, K, G] {

  def mapReduce(
    input: M,
    mapper: A => (K, B),
    zero: B,
    op: (B, B) => B): G
}

object MapReducible {

  final def apply[M, A, B, K, G](implicit mra: MapReducible[M, A, B, K, G]): MapReducible[M, A, B, K, G] =
    implicitly[MapReducible[M, A, B, K, G]]

  implicit def mapReduceSeq[A, B, K]: MapReducible[Seq[A], A, B, K, Map[K, B]] =
    new MapReducible[Seq[A], A, B, K, Map[K, B]] {

      def mapReduce(
        input: Seq[A],
        mapper: A => (K, B),
        zero: B,
        reduce: (B, B) => B): Map[K, B] =
        input
          .map(mapper)
          .groupBy(_._1)
          .mapValues(kbs => kbs.map(_._2).foldLeft(zero)(reduce))
          .toMap
    }

  implicit def mapReduceVectorMap[A, B, K]: MapReducible[Vector[A], A, B, K, Map[K, B]] =
    new MapReducible[Vector[A], A, B, K, Map[K, B]] {

      def mapReduce(
        input: Vector[A],
        mapper: A => (K, B),
        zero: B,
        reduce: (B, B) => B): Map[K, B] =
        input
          .map(mapper)
          .groupBy(_._1)
          .mapValues(kbs => kbs.map(_._2).foldLeft(zero)(reduce))
          .toMap
    }

  implicit def mapReduceListMap[A, B, K]: MapReducible[List[A], A, B, K, Map[K, B]] =
    new MapReducible[List[A], A, B, K, Map[K, B]] {

      def mapReduce(
        input: List[A],
        mapper: A => (K, B),
        zero: B,
        reduce: (B, B) => B): Map[K, B] =
        input
          .map(mapper)
          .groupBy(_._1)
          .mapValues(kbs => kbs.map(_._2).foldLeft(zero)(reduce))
          .toMap
    }

}
