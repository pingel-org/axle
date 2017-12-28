package axle.algebra

import scala.annotation.implicitNotFound

@implicitNotFound("Witness not found for MapReducible[${M}]")
trait MapReducible[M[_]] {

  def mapReduce[A, B, K](
    input:  M[A],
    mapper: A => (K, B),
    zero:   B,
    op:     (B, B) => B): M[(K, B)]
}

object MapReducible {

  final def apply[M[_]](implicit mra: MapReducible[M]): MapReducible[M] =
    implicitly[MapReducible[M]]

  implicit val mapReduceSeq: MapReducible[Seq] =
    new MapReducible[Seq] {

      def mapReduce[A, B, K](
        input:  Seq[A],
        mapper: A => (K, B),
        zero:   B,
        reduce: (B, B) => B): Seq[(K, B)] =
        input
          .map(mapper)
          .groupBy(_._1)
          .mapValues(kbs => kbs.map(_._2).foldLeft(zero)(reduce))
          .toSeq
    }

  implicit val mapReduceVector: MapReducible[Vector] =
    new MapReducible[Vector] {

      def mapReduce[A, B, K](
        input:  Vector[A],
        mapper: A => (K, B),
        zero:   B,
        reduce: (B, B) => B): Vector[(K, B)] =
        input
          .map(mapper)
          .groupBy(_._1)
          .mapValues(kbs => kbs.map(_._2).foldLeft(zero)(reduce))
          .toVector
    }

  implicit val mapReduceListMap: MapReducible[List] =
    new MapReducible[List] {

      def mapReduce[A, B, K](
        input:  List[A],
        mapper: A => (K, B),
        zero:   B,
        reduce: (B, B) => B): List[(K, B)] =
        input
          .map(mapper)
          .groupBy(_._1)
          .mapValues(kbs => kbs.map(_._2).foldLeft(zero)(reduce))
          .toList
    }

}
