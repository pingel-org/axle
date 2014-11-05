package axle.algebra

import scala.reflect.ClassTag
import scala.collection.parallel.immutable.ParSeq

trait Indexed[C[_]] {

  def at[A: ClassTag](xs: C[A])(i: Int): A
}

object Indexed {

  implicit def indexedSeq: Indexed[Seq] = new Indexed[Seq] {

    def at[A: ClassTag](seq: Seq[A])(i: Int): A = seq(i)
  }

  implicit def indexedList: Indexed[List] = new Indexed[List] {

    def at[A: ClassTag](list: List[A])(i: Int): A = list(i)
  }

  implicit def indexedParSeq: Indexed[ParSeq] = new Indexed[ParSeq] {

    def at[A: ClassTag](ps: ParSeq[A])(i: Int): A = ps(i)
  }

  implicit def indexedIndexedSeq: Indexed[scala.collection.immutable.IndexedSeq] = new Indexed[scala.collection.immutable.IndexedSeq] {

    def at[A: ClassTag](is: scala.collection.immutable.IndexedSeq[A])(i: Int): A = is(i)
  }

}
