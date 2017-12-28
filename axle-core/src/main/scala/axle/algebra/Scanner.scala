package axle.algebra

import scala.annotation.implicitNotFound

@implicitNotFound("Witness not found for Scanner[${S}]")
trait Scanner[S[_]] {

  def scanLeft[A, B](s: S[A])(z: B)(op: (B, A) => B): S[B]
}

object Scanner {

  final def apply[S[_]](implicit ev: Scanner[S]): Scanner[S] = ev

  implicit val indexedSeq: Scanner[IndexedSeq] =
    new Scanner[IndexedSeq] {

      def scanLeft[A, B](s: IndexedSeq[A])(z: B)(op: (B, A) => B): IndexedSeq[B] =
        s.scanLeft(z)(op)
    }

  implicit val list: Scanner[List] =
    new Scanner[List] {

      def scanLeft[A, B](s: List[A])(z: B)(op: (B, A) => B): List[B] =
        s.scanLeft(z)(op)
    }

}
