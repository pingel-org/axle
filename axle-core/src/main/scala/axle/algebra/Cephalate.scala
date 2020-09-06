package axle.algebra

import scala.annotation.implicitNotFound

/**
  * Cephalate
  * 
  * "having a head or an enlargement suggesting a head" (Merriam-Webster)
  * 
  */

@implicitNotFound("Witness not found for Cephalate[${F}]")
trait Cephalate[F[_]] {

  def nil[A]: F[A]

  def head[A](xs: F[A]): A

  def tail[A](xs: F[A]): F[A]

  def cons[A](xs: F[A], x: A): F[A]
}

object Cephalate {

  def apply[F[_]](implicit cephalate: Cephalate[F]): Cephalate[F] =
    cephalate

  implicit val cephalateSeq: Cephalate[Seq] =
    new Cephalate[Seq] {

      def nil[A]: Seq[A] = Seq.empty[A]

      def head[A](xs: Seq[A]): A = xs.head

      def tail[A](xs: Seq[A]): Seq[A] = xs.tail

      def cons[A](xs: Seq[A], x: A): Seq[A] = x +: xs

    }

}
