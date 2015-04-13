package axle.algebra

import scala.reflect.ClassTag
import scala.annotation.implicitNotFound

@implicitNotFound("No member of typeclass MapFrom found for type ${C}")
trait MapFrom[C[_]] {

  def toMap[K: ClassTag, V: ClassTag](t: C[(K, V)]): Map[K, V]
}

object MapFrom {

  def apply[C[_]: MapFrom]: MapFrom[C] = implicitly[MapFrom[C]]

  implicit def mapFromSeq: MapFrom[Seq] = new MapFrom[Seq] {

    def toMap[K: ClassTag, V: ClassTag](seq: Seq[(K, V)]): Map[K, V] =
      seq.toMap
  }
}
