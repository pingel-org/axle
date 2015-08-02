package axle.algebra

import scala.reflect.ClassTag
import scala.annotation.implicitNotFound
import scala.collection.parallel.immutable.ParSeq

@implicitNotFound("Witness not found for Indexed[${C}, ${I}, ${A}]")
trait Indexed[C, I, A] {

  def at(xs: C)(i: I): A
}

object Indexed {

  @inline final def apply[C, I, A](implicit ev: Indexed[C, I, A]): Indexed[C, I, A] = ev

  implicit def indexedSeq[A]: Indexed[Seq[A], Int, A] =
    new Indexed[Seq[A], Int, A] {
      def at(seq: Seq[A])(i: Int): A = seq(i)
    }

  implicit def indexedIndexedSeq[A]: Indexed[IndexedSeq[A], Int, A] =
    new Indexed[IndexedSeq[A], Int, A] {
      def at(is: IndexedSeq[A])(i: Int): A = is(i)
    }

  implicit def indexedList[A]: Indexed[List[A], Int, A] =
    new Indexed[List[A], Int, A] {
      def at(list: List[A])(i: Int): A = list(i)
    }

  implicit def vectorIndexed[A]: Indexed[Vector[A], Int, A] =
    new Indexed[Vector[A], Int, A] {
      def at(vector: Vector[A])(i: Int): A = vector(i)
    }

  implicit def indexedParSeq[A]: Indexed[ParSeq[A], Int, A] =
    new Indexed[ParSeq[A], Int, A] {
      def at(ps: ParSeq[A])(i: Int): A = ps(i)
    }

  import scala.collection.immutable.{ IndexedSeq => MIS }

  implicit def indexedImmutableIndexedSeq[A]: Indexed[MIS[A], Int, A] =
    new Indexed[MIS[A], Int, A] {
      def at(is: MIS[A])(i: Int): A = is(i)
    }

}
