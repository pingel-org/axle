package axle.algebra

import scala.reflect.ClassTag
import scala.annotation.implicitNotFound

@implicitNotFound("Witness not found for MapReducible[${M}]")
trait MapReducible[M[_]] {

  def mapReduce[A: ClassTag, B: ClassTag, K: ClassTag](
    input: M[A],
    mapper: A => (K, B),
    zero: B,
    op: (B, B) => B): M[(K, B)]
}

object MapReducible {

  @inline final def apply[M[_]: MapReducible]: MapReducible[M] = implicitly[MapReducible[M]]

  implicit def mapReduceSeq: MapReducible[Seq] = new MapReducible[Seq] {

    def mapReduce[A: ClassTag, B: ClassTag, K: ClassTag](
      input: Seq[A],
      mapper: A => (K, B),
      zero: B,
      reduce: (B, B) => B): Seq[(K, B)] =
      input.map(mapper).groupBy(_._1).mapValues(kbs => kbs.map(_._2).foldLeft(zero)(reduce)).toSeq
  }

  implicit def mapReduceVector: MapReducible[Vector] = new MapReducible[Vector] {

    def mapReduce[A: ClassTag, B: ClassTag, K: ClassTag](
      input: Vector[A],
      mapper: A => (K, B),
      zero: B,
      reduce: (B, B) => B): Vector[(K, B)] =
      input.map(mapper).groupBy(_._1).mapValues(kbs => kbs.map(_._2).foldLeft(zero)(reduce)).toVector
  }

  implicit def mapReduceList: MapReducible[List] = new MapReducible[List] {

    def mapReduce[A: ClassTag, B: ClassTag, K: ClassTag](
      input: List[A],
      mapper: A => (K, B),
      zero: B,
      reduce: (B, B) => B): List[(K, B)] =
      input.map(mapper).groupBy(_._1).mapValues(kbs => kbs.map(_._2).foldLeft(zero)(reduce)).toList
  }

}
