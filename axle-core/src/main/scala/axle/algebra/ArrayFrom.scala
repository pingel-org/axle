package axle.algebra

import scala.reflect.ClassTag
import scala.annotation.implicitNotFound

@implicitNotFound("Witness not found for ArrayFrom[${C}]")
trait ArrayFrom[C[_]] {

  def toArray[A: ClassTag](af: C[A]): Array[A]
}

object ArrayFrom {

  @inline final def apply[C[_]: ArrayFrom]: ArrayFrom[C] = implicitly[ArrayFrom[C]]

  implicit def arrayFromSeq: ArrayFrom[Seq] = new ArrayFrom[Seq] {

    def toArray[A: ClassTag](af: Seq[A]): Array[A] = af.toArray
  }

}
