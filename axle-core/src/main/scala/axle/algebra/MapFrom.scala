package axle.algebra

import scala.reflect.ClassTag
import scala.annotation.implicitNotFound

@implicitNotFound("Witness not found for MapFrom[${C}]")
trait MapFrom[C[_]] {

  def toMap[K: ClassTag, V: ClassTag](t: C[(K, V)]): Map[K, V]
}

object MapFrom {

  @inline final def apply[C[_]: MapFrom]: MapFrom[C] = implicitly[MapFrom[C]]

  implicit def mapFromSeq: MapFrom[Seq] = new MapFrom[Seq] {

    def toMap[K: ClassTag, V: ClassTag](seq: Seq[(K, V)]): Map[K, V] =
      seq.toMap
  }
}
