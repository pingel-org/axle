package axle.algebra

import scala.reflect.ClassTag

trait Finite[C[_]] {

  def size[A: ClassTag](t: C[A]): Long
}

object Finite {

  implicit def finiteSeq: Finite[Seq] = new Finite[Seq] {

    def size[A: ClassTag](seq: Seq[A]): Long = seq.size
  }
}
