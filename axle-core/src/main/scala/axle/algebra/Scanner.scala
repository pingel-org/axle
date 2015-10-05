package axle.algebra

import scala.annotation.implicitNotFound

@implicitNotFound("Witness not found for Scanner[${S}, ${A}, ${B}, ${T}]")
trait Scanner[S, A, B, T] {

  def scanLeft(s: S)(z: B)(op: (B, A) => B): T
}

object Scanner {

  final def apply[S, A, B, T](implicit ev: Scanner[S, A, B, T]): Scanner[S, A, B, T] = ev

  implicit def indexedSeq[A, B]: Scanner[IndexedSeq[A], A, B, IndexedSeq[B]] =
    new Scanner[IndexedSeq[A], A, B, IndexedSeq[B]] {

      def scanLeft(s: IndexedSeq[A])(z: B)(op: (B, A) => B): IndexedSeq[B] =
        s.scanLeft(z)(op)
    }

  implicit def list[A, B]: Scanner[List[A], A, B, List[B]] =
    new Scanner[List[A], A, B, List[B]] {

      def scanLeft(s: List[A])(z: B)(op: (B, A) => B): List[B] =
        s.scanLeft(z)(op)
    }

}