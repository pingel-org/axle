package axle.algebra

import scala.annotation.implicitNotFound
import scala.reflect.ClassTag

@implicitNotFound("Witness not found for ArrayFrom[${C}, ${A}]")
trait ArrayFrom[C, A] {

  def toArray(af: C): Array[A]
}

object ArrayFrom {

  final def apply[C, A](implicit af: ArrayFrom[C, A]): ArrayFrom[C, A] = af

  implicit def arrayFromSeq[A: ClassTag]: ArrayFrom[Seq[A], A] = new ArrayFrom[Seq[A], A] {

    def toArray(af: Seq[A]): Array[A] = af.toArray
  }

}
