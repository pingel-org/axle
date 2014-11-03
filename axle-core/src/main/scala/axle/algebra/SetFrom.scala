package axle.algebra

import scala.reflect.ClassTag

trait SetFrom[C[_]] {

  def toSet[A: ClassTag](t: C[A]): Set[A]
}

object SetFrom {

  implicit def sizedSeq: SetFrom[Seq] = new SetFrom[Seq] {

    def toSet[A: ClassTag](seq: Seq[A]): Set[A] = seq.toSet
  }
}
