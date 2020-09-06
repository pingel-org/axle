package axle.algebra

import scala.annotation.implicitNotFound
import spire.math.Real
import spire.math.Rational

@implicitNotFound("Witness not found for Finite[${C}, ${N}]")
trait Finite[C[_], N] {

  def size[A](t: C[A]): N
}

object Finite {

  final def apply[C[_], N](implicit ev: Finite[C, N]): Finite[C, N] = ev

  implicit def finiteCIntRational[C[_]](implicit ev: Finite[C, Int]): Finite[C, Rational] =
    new Finite[C, Rational] {
      def size[A](as: C[A]): Rational = ev.size(as)
    }

  implicit def finiteCIntLong[C[_]](implicit ev: Finite[C, Int]): Finite[C, Long] =
    new Finite[C, Long] {
      def size[A](as: C[A]): Long = ev.size(as).toLong
    }

  implicit def finiteCIntDouble[C[_]](implicit ev: Finite[C, Int]): Finite[C, Double] =
    new Finite[C, Double] {
      def size[A](as: C[A]): Double = ev.size(as).toDouble
    }

  implicit def finiteCIntReal[C[_]](implicit ev: Finite[C, Int]): Finite[C, Real] =
    new Finite[C, Real] {
      def size[A](as: C[A]): Real = ev.size(as)
    }

  implicit val finiteSeq: Finite[Seq, Int] =
    new Finite[Seq, Int] {

      def size[A](seq: Seq[A]): Int = seq.size
    }

  implicit val finiteList: Finite[List, Int] =
    new Finite[List, Int] {

      def size[A](list: List[A]): Int = list.length
    }

  implicit val finiteVector: Finite[Vector, Int] =
    new Finite[Vector, Int] {

      def size[A](vector: Vector[A]): Int =
        vector.length
    }

  implicit val finiteIndexedSeq: Finite[IndexedSeq, Int] =
    new Finite[IndexedSeq, Int] {

      def size[A](is: IndexedSeq[A]): Int = is.length
    }

}
