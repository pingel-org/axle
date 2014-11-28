package axle.algebra

import scala.reflect.ClassTag
import scala.collection.parallel.immutable.ParSeq

trait Finite[C[_]] {

  def size[A: ClassTag](t: C[A]): Long
}

object Finite {

  implicit def finiteSeq: Finite[Seq] = new Finite[Seq] {

    def size[A: ClassTag](seq: Seq[A]): Long = seq.size
  }

  implicit def finiteList: Finite[List] = new Finite[List] {

    def size[A: ClassTag](list: List[A]): Long =
      list.length
  }

  implicit def finiteVector: Finite[Vector] = new Finite[Vector] {

    def size[A: ClassTag](vector: Vector[A]): Long =
      vector.length
  }

  implicit def finiteParSeq: Finite[ParSeq] = new Finite[ParSeq] {

    def size[A: ClassTag](ps: ParSeq[A]): Long =
      ps.length
  }

  implicit def finiteIndexedSeq: Finite[scala.collection.immutable.IndexedSeq] = new Finite[scala.collection.immutable.IndexedSeq] {

    def size[A: ClassTag](is: scala.collection.immutable.IndexedSeq[A]): Long =
      is.length
  }

}
