package axle

import scala.collection.parallel.immutable.ParSeq

import spire.algebra._

import axle.algebra.Aggregatable
import axle.algebra.Finite
import axle.algebra.Indexed
import axle.algebra.Talliable

package object parallel {

  implicit val aggregatableParSeq =
    new Aggregatable[ParSeq] {
      def aggregate[A, B](ps: ParSeq[A])(zeroValue: B)(seqOp: (B, A) => B, combOp: (B, B) => B): B =
        ps.aggregate(zeroValue)(seqOp, combOp)
    }

  implicit val finiteParSeq: Finite[ParSeq, Int] =
    new Finite[ParSeq, Int] {

      def size[A](ps: ParSeq[A]): Int =
        ps.length
    }

  implicit val indexedParSeq: Indexed[ParSeq, Int] =
    new Indexed[ParSeq, Int] {

      def at[A](ps: ParSeq[A])(i: Int): A = ps(i)

      def take[A](xs: ParSeq[A])(i: Int): ParSeq[A] = xs.take(i)

      def drop[A](xs: ParSeq[A])(i: Int): ParSeq[A] = xs.drop(i)
    }

  implicit val tallyParSeq =
    new Talliable[ParSeq] {

      def tally[A, N](xs: ParSeq[A])(implicit ring: CRing[N]): Map[A, N] = {
        val mapCR = new spire.std.MapCRng[A, N]()
        xs.aggregate(Map.empty[A, N].withDefaultValue(ring.zero))(
          (m, x) => m + (x -> ring.plus(m(x), ring.one)),
          mapCR.plus)
      }
    }

}
