package axle.algebra

import scala.reflect.ClassTag

trait MapFrom[C[_]] {

  def toMap[K: ClassTag, V: ClassTag](t: C[(K, V)]): Map[K, V]
}

object MapFrom {

  implicit def mapFromSeq: MapFrom[Seq] = new MapFrom[Seq] {

    def toMap[K: ClassTag, V: ClassTag](seq: Seq[(K, V)]): Map[K, V] =
      seq.toMap
  }
}
