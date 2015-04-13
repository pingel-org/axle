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

  def apply[M[_]: MapReducible]: MapReducible[M] = implicitly[MapReducible[M]]

  implicit def mapReduceSeq: MapReducible[Seq] = new MapReducible[Seq] {

    def mapReduce[A: ClassTag, B: ClassTag, K: ClassTag](
      input: Seq[A],
      mapper: A => (K, B),
      zero: B,
      reduce: (B, B) => B): Seq[(K, B)] = ???
  }
}
