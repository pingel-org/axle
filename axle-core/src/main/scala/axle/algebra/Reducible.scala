package axle.algebra

import scala.reflect.ClassTag

trait Reducible[R[_]] {

  def reduceOption[A: ClassTag](as: R[A])(op: (A, A) => A): Option[A]
}

object Reducible {

  implicit def reduceSeq: Reducible[Seq] = new Reducible[Seq] {

    def reduceOption[A: ClassTag](as: Seq[A])(op: (A, A) => A): Option[A] =
      as.reduceOption(op)
  }

  implicit def reduceIndexedSeq: Reducible[IndexedSeq] =
    new Reducible[IndexedSeq] {
      def reduceOption[A: ClassTag](is: IndexedSeq[A])(op: (A, A) => A): Option[A] =
        is.reduceOption(op)
    }

}
