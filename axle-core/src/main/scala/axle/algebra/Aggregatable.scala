package axle.algebra

import scala.annotation.implicitNotFound
import scala.collection.parallel.immutable.ParSeq

import spire.algebra.Eq
import spire.algebra.Ring
import spire.implicits.MapRng
import spire.implicits.additiveSemigroupOps

import scala.annotation.implicitNotFound

@implicitNotFound("Witness not found for Aggregatable[${F}, ${A}, ${B}]")
trait Aggregatable[F, A, B] {

  def aggregate(xs: F)(zeroValue: B)(seqOp: (B, A) => B, combOp: (B, B) => B): B

}

object Aggregatable {

  def apply[F, A, B](implicit aggFA: Aggregatable[F, A, B]): Aggregatable[F, A, B] =
    implicitly[Aggregatable[F, A, B]]

  implicit def aggregatableSeq[A, B] =
    new Aggregatable[Seq[A], A, B] {
      def aggregate(as: Seq[A])(zeroValue: B)(seqOp: (B, A) => B, combOp: (B, B) => B): B =
        as.aggregate(zeroValue)(seqOp, combOp)
    }

  implicit def aggregatableList[A, B] =
    new Aggregatable[List[A], A, B] {
      def aggregate(as: List[A])(zeroValue: B)(seqOp: (B, A) => B, combOp: (B, B) => B): B =
        as.aggregate(zeroValue)(seqOp, combOp)
    }

  implicit def aggregatableVector[A, B] =
    new Aggregatable[Vector[A], A, B] {
      def aggregate(as: Vector[A])(zeroValue: B)(seqOp: (B, A) => B, combOp: (B, B) => B): B =
        as.aggregate(zeroValue)(seqOp, combOp)
    }

  implicit def aggregatableParSeq[A, B] =
    new Aggregatable[ParSeq[A], A, B] {
      def aggregate(ps: ParSeq[A])(zeroValue: B)(seqOp: (B, A) => B, combOp: (B, B) => B): B =
        ps.aggregate(zeroValue)(seqOp, combOp)
    }

  implicit def aggregatableIndexedSeq[A, B] =
    new Aggregatable[IndexedSeq[A], A, B] {
      def aggregate(is: IndexedSeq[A])(zeroValue: B)(seqOp: (B, A) => B, combOp: (B, B) => B): B =
        is.aggregate(zeroValue)(seqOp, combOp)
    }

  implicit def aggregatableIterable[A, B] =
    new Aggregatable[Iterable[A], A, B] {
      def aggregate(i: Iterable[A])(zeroValue: B)(seqOp: (B, A) => B, combOp: (B, B) => B): B =
        i.aggregate(zeroValue)(seqOp, combOp)
    }

  import scala.collection.immutable.{ IndexedSeq => ImmIndexedSeq }

  implicit def aggregatableImmutableIndexedSeq[A, B] =
    new Aggregatable[ImmIndexedSeq[A], A, B] {

      def aggregate(is: ImmIndexedSeq[A])(zeroValue: B)(seqOp: (B, A) => B, combOp: (B, B) => B): B =
        is.aggregate(zeroValue)(seqOp, combOp)
    }

}
