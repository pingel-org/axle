package axle.algebra

import scala.reflect.ClassTag
import scala.annotation.implicitNotFound
import scala.collection.parallel.immutable.ParSeq

@implicitNotFound("Witness not found for Indexed[${C}, ${IndexT}]")
trait Indexed[C[_], IndexT] {

  def at[A: ClassTag](xs: C[A])(i: IndexT): A
}

object Indexed {

  @inline final def apply[C[_], IndexT](implicit ev: Indexed[C, IndexT]): Indexed[C, IndexT] = ev

  implicit def indexedSeq: Indexed[Seq, Int] =
    new Indexed[Seq, Int] {
      def at[A: ClassTag](seq: Seq[A])(i: Int): A = seq(i)
    }

  implicit def indexedIndexedSeq: Indexed[IndexedSeq, Int] =
    new Indexed[IndexedSeq, Int] {
      def at[A: ClassTag](is: IndexedSeq[A])(i: Int): A = is(i)
    }

  implicit def indexedList: Indexed[List, Int] =
    new Indexed[List, Int] {
      def at[A: ClassTag](list: List[A])(i: Int): A = list(i)
    }

  implicit def vectorIndexed: Indexed[Vector, Int] =
    new Indexed[Vector, Int] {
      def at[A: ClassTag](vector: Vector[A])(i: Int): A = vector(i)
    }

  implicit def indexedParSeq: Indexed[ParSeq, Int] =
    new Indexed[ParSeq, Int] {
      def at[A: ClassTag](ps: ParSeq[A])(i: Int): A = ps(i)
    }

  implicit def indexedImmutableIndexedSeq: Indexed[scala.collection.immutable.IndexedSeq, Int] =
    new Indexed[scala.collection.immutable.IndexedSeq, Int] {
      def at[A: ClassTag](is: scala.collection.immutable.IndexedSeq[A])(i: Int): A = is(i)
    }

}
