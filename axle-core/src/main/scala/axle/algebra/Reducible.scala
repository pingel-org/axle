package axle.algebra

import scala.annotation.implicitNotFound

@implicitNotFound("Witness not found for Reducible[${R}]")
trait Reducible[R[_]] {

  def reduceOption[A](as: R[A])(op: (A, A) => A): Option[A]
}

object Reducible {

  final def apply[R[_]](implicit ra: Reducible[R]): Reducible[R] =
    implicitly[Reducible[R]]

  implicit val reduceSeq: Reducible[Seq] = new Reducible[Seq] {
    def reduceOption[A](as: Seq[A])(op: (A, A) => A): Option[A] =
      as.reduceOption(op)
  }

  implicit val reduceIndexedSeq: Reducible[IndexedSeq] =
    new Reducible[IndexedSeq] {
      def reduceOption[A](is: IndexedSeq[A])(op: (A, A) => A): Option[A] =
        is.reduceOption(op)
    }

}
