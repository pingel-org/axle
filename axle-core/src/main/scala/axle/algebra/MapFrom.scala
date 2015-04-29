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

  implicit def mapFromVector: MapFrom[Vector] = new MapFrom[Vector] {

    def toMap[K: ClassTag, V: ClassTag](vector: Vector[(K, V)]): Map[K, V] =
      vector.toMap
  }

  implicit def mapFromList: MapFrom[List] = new MapFrom[List] {

    def toMap[K: ClassTag, V: ClassTag](list: List[(K, V)]): Map[K, V] =
      list.toMap
  }

}
