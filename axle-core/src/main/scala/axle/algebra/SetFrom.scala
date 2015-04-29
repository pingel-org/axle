package axle.algebra

import scala.reflect.ClassTag
import scala.annotation.implicitNotFound

@implicitNotFound("Witness not found for SetFrom[${C}]")
trait SetFrom[C[_]] {

  def toSet[A: ClassTag](t: C[A]): Set[A]
}

object SetFrom {

  @inline final def apply[C[_]: SetFrom]: SetFrom[C] = implicitly[SetFrom[C]]

  implicit def setFromSeq: SetFrom[Seq] = new SetFrom[Seq] {
    def toSet[A: ClassTag](seq: Seq[A]): Set[A] = seq.toSet
  }

  implicit def setFromList: SetFrom[List] = new SetFrom[List] {
    def toSet[A: ClassTag](list: List[A]): Set[A] = list.toSet
  }

  implicit def setFromVector: SetFrom[Vector] = new SetFrom[Vector] {
    def toSet[A: ClassTag](vector: Vector[A]): Set[A] = vector.toSet
  }

}
