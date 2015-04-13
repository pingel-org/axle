package axle.algebra

import scala.reflect.ClassTag
import scala.annotation.implicitNotFound

@implicitNotFound("Witness not found for SetFrom[${C}]")
trait SetFrom[C[_]] {

  def toSet[A: ClassTag](t: C[A]): Set[A]
}

object SetFrom {

  @inline final def apply[C[_]: SetFrom]: SetFrom[C] = implicitly[SetFrom[C]]

  implicit def sizedSeq: SetFrom[Seq] = new SetFrom[Seq] {

    def toSet[A: ClassTag](seq: Seq[A]): Set[A] = seq.toSet
  }
}
