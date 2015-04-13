package axle.algebra

import scala.reflect.ClassTag
import scala.annotation.implicitNotFound

@implicitNotFound("No member of typeclass SetFrom found for type ${C}")
trait SetFrom[C[_]] {

  def toSet[A: ClassTag](t: C[A]): Set[A]
}

object SetFrom {

  def apply[C[_]: SetFrom]: SetFrom[C] = implicitly[SetFrom[C]]

  implicit def sizedSeq: SetFrom[Seq] = new SetFrom[Seq] {

    def toSet[A: ClassTag](seq: Seq[A]): Set[A] = seq.toSet
  }
}
