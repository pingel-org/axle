package axle.algebra

import scala.annotation.implicitNotFound
import scala.reflect.ClassTag
import scala.collection.parallel.immutable.ParSeq
import spire.math.Rational

@implicitNotFound("No member of typeclass Finite found for types ${C}, ${S}")
trait Finite[C[_], S] {

  def size[A: ClassTag](t: C[A]): S
}

object Finite {

  implicit def finiteCIntRational[C[_]](implicit ev: Finite[C, Int]): Finite[C, Rational] =
    new Finite[C, Rational] {
      def size[A: ClassTag](xs: C[A]): Rational = ev.size(xs)
    }

  implicit def finiteSeq: Finite[Seq, Int] =
    new Finite[Seq, Int] {

      def size[A: ClassTag](seq: Seq[A]): Int = seq.size
    }

  implicit def finiteList: Finite[List, Int] =
    new Finite[List, Int] {

      def size[A: ClassTag](list: List[A]): Int = list.length
    }

  implicit def finiteVector: Finite[Vector, Int] =
    new Finite[Vector, Int] {

      def size[A: ClassTag](vector: Vector[A]): Int =
        vector.length
    }

  implicit def finiteParSeq: Finite[ParSeq, Int] =
    new Finite[ParSeq, Int] {

      def size[A: ClassTag](ps: ParSeq[A]): Int =
        ps.length
    }

  implicit def finiteIndexedSeq: Finite[scala.collection.immutable.IndexedSeq, Int] =
    new Finite[scala.collection.immutable.IndexedSeq, Int] {

      def size[A: ClassTag](is: scala.collection.immutable.IndexedSeq[A]): Int =
        is.length
    }

}
