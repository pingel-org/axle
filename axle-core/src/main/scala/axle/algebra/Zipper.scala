package axle.algebra

import scala.annotation.implicitNotFound

@implicitNotFound("Witness not found for Zipper[${M}]")
trait Zipper[M[_]] {

  def zip[L, R](left: M[L], right: M[R]): M[(L, R)]

  def unzip[L, R](zipped: M[(L, R)]): (M[L], M[R])
}

object Zipper {

  def apply[M[_]](implicit zipper: Zipper[M]): Zipper[M] =
    zipper

  implicit val zipSeq: Zipper[Seq] =
    new Zipper[Seq] {

      def zip[A, B](left: Seq[A], right: Seq[B]): Seq[(A, B)] =
        left.zip(right)

      def unzip[A, B](zipped: Seq[(A, B)]): (Seq[A], Seq[B]) =
        zipped.unzip
    }

  implicit val zipList: Zipper[List] =
    new Zipper[List] {

      def zip[A, B](left: List[A], right: List[B]): List[(A, B)] =
        left.zip(right)

      def unzip[A, B](zipped: List[(A, B)]): (List[A], List[B]) =
        zipped.unzip
    }

  implicit val zipIndexedSeq: Zipper[IndexedSeq] =
    new Zipper[IndexedSeq] {

      def zip[A, B](left: IndexedSeq[A], right: IndexedSeq[B]): IndexedSeq[(A, B)] =
        left.zip(right)

      def unzip[A, B](zipped: IndexedSeq[(A, B)]): (IndexedSeq[A], IndexedSeq[B]) =
        zipped.unzip
    }

}
