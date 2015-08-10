package axle.algebra

import scala.annotation.implicitNotFound

@implicitNotFound("Witness not found for SetFrom[${C}, ${A}]")
trait SetFrom[C, A] {

  def toSet(t: C): Set[A]
}

object SetFrom {

  final def apply[C, A](implicit sfa: SetFrom[C, A]): SetFrom[C, A] =
    implicitly[SetFrom[C, A]]

  implicit def setFromSeq[A]: SetFrom[Seq[A], A] =
    new SetFrom[Seq[A], A] {
      def toSet(seq: Seq[A]): Set[A] = seq.toSet
    }

  implicit def setFromList[A]: SetFrom[List[A], A] =
    new SetFrom[List[A], A] {
      def toSet(list: List[A]): Set[A] = list.toSet
    }

  implicit def setFromVector[A]: SetFrom[Vector[A], A] =
    new SetFrom[Vector[A], A] {
      def toSet(vector: Vector[A]): Set[A] = vector.toSet
    }

}
