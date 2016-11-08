package axle.algebra

import scala.annotation.implicitNotFound

@implicitNotFound("Witness not found for Reducible[${R}]")
trait Reducible[R, A] {

  def reduceOption(as: R)(op: (A, A) => A): Option[A]
}

object Reducible {

  final def apply[R, A](implicit ra: Reducible[R, A]): Reducible[R, A] =
    implicitly[Reducible[R, A]]

  implicit def reduceSeq[A]: Reducible[Seq[A], A] = new Reducible[Seq[A], A] {

    def reduceOption(as: Seq[A])(op: (A, A) => A): Option[A] =
      as.reduceOption(op)
  }

  implicit def reduceIndexedSeq[A]: Reducible[IndexedSeq[A], A] =
    new Reducible[IndexedSeq[A], A] {
      def reduceOption(is: IndexedSeq[A])(op: (A, A) => A): Option[A] =
        is.reduceOption(op)
    }

}
