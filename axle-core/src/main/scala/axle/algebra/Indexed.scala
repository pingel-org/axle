package axle.algebra

import scala.annotation.implicitNotFound

@implicitNotFound("Witness not found for Indexed[${C}, ${I}]")
trait Indexed[C[_], I] {

  def at[A](xs: C[A])(i: I): A

  def take[A](xs: C[A])(i: I): C[A]

  def drop[A](xs: C[A])(i: I): C[A]
}

object Indexed {

  final def apply[C[_], I](implicit ev: Indexed[C, I]): Indexed[C, I] = ev

  implicit val indexedSeq: Indexed[Seq, Int] =
    new Indexed[Seq, Int] {

      def at[A](seq: Seq[A])(i: Int): A = seq(i)

      def take[A](xs: Seq[A])(i: Int): Seq[A] = xs.take(i)

      def drop[A](xs: Seq[A])(i: Int): Seq[A] = xs.drop(i)
    }

  implicit val indexedIndexedSeq: Indexed[IndexedSeq, Int] =
    new Indexed[IndexedSeq, Int] {

      def at[A](is: IndexedSeq[A])(i: Int): A = is(i)

      def take[A](xs: IndexedSeq[A])(i: Int): IndexedSeq[A] = xs.take(i)

      def drop[A](xs: IndexedSeq[A])(i: Int): IndexedSeq[A] = xs.drop(i)
    }

  implicit val indexedList: Indexed[List, Int] =
    new Indexed[List, Int] {

      def at[A](list: List[A])(i: Int): A = list(i)

      def take[A](xs: List[A])(i: Int): List[A] = xs.take(i)

      def drop[A](xs: List[A])(i: Int): List[A] = xs.drop(i)
    }

  implicit val vectorIndexed: Indexed[Vector, Int] =
    new Indexed[Vector, Int] {

      def at[A](vector: Vector[A])(i: Int): A = vector(i)

      def take[A](xs: Vector[A])(i: Int): Vector[A] = xs.take(i)

      def drop[A](xs: Vector[A])(i: Int): Vector[A] = xs.drop(i)
    }

}
