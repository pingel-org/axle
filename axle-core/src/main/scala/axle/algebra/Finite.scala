package axle.algebra

import scala.annotation.implicitNotFound
import scala.collection.parallel.immutable.ParSeq

import spire.math.Rational

@implicitNotFound("Witness not found for Finite[${C}, ${S}]")
trait Finite[C, S] {

  def size(t: C): S
}

object Finite {

  @inline final def apply[C, S](implicit ev: Finite[C, S]): Finite[C, S] = ev

  implicit def finiteCIntRational[C](implicit ev: Finite[C, Int]): Finite[C, Rational] =
    new Finite[C, Rational] {
      def size(xs: C): Rational = ev.size(xs)
    }

  implicit def finiteCIntLong[C](implicit ev: Finite[C, Int]): Finite[C, Long] =
    new Finite[C, Long] {
      def size(xs: C): Long = ev.size(xs).toLong
    }

  implicit def finiteSeq[T]: Finite[Seq[T], Int] =
    new Finite[Seq[T], Int] {

      def size(seq: Seq[T]): Int = seq.size
    }

  implicit def finiteList[T]: Finite[List[T], Int] =
    new Finite[List[T], Int] {

      def size(list: List[T]): Int = list.length
    }

  implicit def finiteVector[T]: Finite[Vector[T], Int] =
    new Finite[Vector[T], Int] {

      def size(vector: Vector[T]): Int =
        vector.length
    }

  implicit def finiteParSeq[T]: Finite[ParSeq[T], Int] =
    new Finite[ParSeq[T], Int] {

      def size(ps: ParSeq[T]): Int =
        ps.length
    }

  implicit def finiteIndexedSeq[T]: Finite[IndexedSeq[T], Int] =
    new Finite[IndexedSeq[T], Int] {

      def size(is: IndexedSeq[T]): Int = is.length
    }

  implicit def finiteImmutableIndexedSeq[T]: Finite[scala.collection.immutable.IndexedSeq[T], Int] =
    new Finite[scala.collection.immutable.IndexedSeq[T], Int] {

      def size(is: scala.collection.immutable.IndexedSeq[T]): Int =
        is.length
    }
  
}
