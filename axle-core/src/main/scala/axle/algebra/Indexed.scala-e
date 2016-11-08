package axle.algebra

import scala.annotation.implicitNotFound
import scala.collection.parallel.immutable.ParSeq

@implicitNotFound("Witness not found for Indexed[${C}, ${I}, ${A}]")
trait Indexed[C, I, A] {

  def at(xs: C)(i: I): A

  def take(xs: C)(i: I): C

  def drop(xs: C)(i: I): C
}

object Indexed {

  final def apply[C, I, A](implicit ev: Indexed[C, I, A]): Indexed[C, I, A] = ev

  implicit def indexedSeq[A]: Indexed[Seq[A], Int, A] =
    new Indexed[Seq[A], Int, A] {

      def at(seq: Seq[A])(i: Int): A = seq(i)

      def take(xs: Seq[A])(i: Int): Seq[A] = xs.take(i)

      def drop(xs: Seq[A])(i: Int): Seq[A] = xs.drop(i)
    }

  implicit def indexedIndexedSeq[A]: Indexed[IndexedSeq[A], Int, A] =
    new Indexed[IndexedSeq[A], Int, A] {

      def at(is: IndexedSeq[A])(i: Int): A = is(i)

      def take(xs: IndexedSeq[A])(i: Int): IndexedSeq[A] = xs.take(i)

      def drop(xs: IndexedSeq[A])(i: Int): IndexedSeq[A] = xs.drop(i)
    }

  implicit def indexedList[A]: Indexed[List[A], Int, A] =
    new Indexed[List[A], Int, A] {

      def at(list: List[A])(i: Int): A = list(i)

      def take(xs: List[A])(i: Int): List[A] = xs.take(i)

      def drop(xs: List[A])(i: Int): List[A] = xs.drop(i)
    }

  implicit def vectorIndexed[A]: Indexed[Vector[A], Int, A] =
    new Indexed[Vector[A], Int, A] {

      def at(vector: Vector[A])(i: Int): A = vector(i)

      def take(xs: Vector[A])(i: Int): Vector[A] = xs.take(i)

      def drop(xs: Vector[A])(i: Int): Vector[A] = xs.drop(i)
    }

  implicit def indexedParSeq[A]: Indexed[ParSeq[A], Int, A] =
    new Indexed[ParSeq[A], Int, A] {

      def at(ps: ParSeq[A])(i: Int): A = ps(i)

      def take(xs: ParSeq[A])(i: Int): ParSeq[A] = xs.take(i)

      def drop(xs: ParSeq[A])(i: Int): ParSeq[A] = xs.drop(i)
    }

  import scala.collection.immutable.{ IndexedSeq => MIS }

  implicit def indexedImmutableIndexedSeq[A]: Indexed[MIS[A], Int, A] =
    new Indexed[MIS[A], Int, A] {

      def at(is: MIS[A])(i: Int): A = is(i)

      def take(xs: MIS[A])(i: Int): MIS[A] = xs.take(i)

      def drop(xs: MIS[A])(i: Int): MIS[A] = xs.take(i)
    }

}
