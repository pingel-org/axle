package axle.algebra

import scala.annotation.implicitNotFound

@implicitNotFound("Witness not found for MapFrom[${C}, ${K}, ${V}]")
trait MapFrom[C, K, V] {

  def toMap(t: C): Map[K, V]
}

object MapFrom {

  @inline final def apply[C, K, V](implicit mfa: MapFrom[C, K, V]): MapFrom[C, K, V] =
    implicitly[MapFrom[C, K, V]]

  implicit def mapFromSeq[K, V]: MapFrom[Seq[(K, V)], K, V] =
    new MapFrom[Seq[(K, V)], K, V] {

      def toMap(seq: Seq[(K, V)]): Map[K, V] =
        seq.toMap
    }

  implicit def mapFromVector[K, V]: MapFrom[Vector[(K, V)], K, V] =
    new MapFrom[Vector[(K, V)], K, V] {

      def toMap(vector: Vector[(K, V)]): Map[K, V] =
        vector.toMap
    }

  implicit def mapFromList[K, V]: MapFrom[List[(K, V)], K, V] =
    new MapFrom[List[(K, V)], K, V] {

      def toMap(list: List[(K, V)]): Map[K, V] =
        list.toMap
    }

}
