package axle.algebra

import scala.annotation.implicitNotFound
import scala.reflect.ClassTag
import scala.collection.parallel.immutable.ParSeq

@implicitNotFound("Witness not found for Functor[${F}]")
trait Functor[F, A, B, G] {

  def map(xs: F)(f: A => B): G
}

object Functor {

  @inline final def apply[F, A, B, G](
    implicit functor: Functor[F, A, B, G]): Functor[F, A, B, G] =
    implicitly[Functor[F, A, B, G]]

  implicit def functorSeq[A, B] =
    new Functor[Seq[A], A, B, Seq[B]] {
      def map(seq: Seq[A])(f: A => B): Seq[B] =
        seq map f
    }

  implicit def IndexedSeqFunctor[A, B]: Functor[IndexedSeq[A], A, B, IndexedSeq[B]] =
    new Functor[IndexedSeq[A], A, B, IndexedSeq[B]] {
      def map(is: IndexedSeq[A])(f: A => B): IndexedSeq[B] =
        is map f
    }

  implicit def ListFunctor[A, B]: Functor[List[A], A, B, List[B]] =
    new Functor[List[A], A, B, List[B]] {
      def map(list: List[A])(f: A => B): List[B] =
        list map f
    }

  implicit def functorVector[A, B]: Functor[Vector[A], A, B, Vector[B]] =
    new Functor[Vector[A], A, B, Vector[B]] {
      def map(ps: Vector[A])(f: A => B): Vector[B] =
        ps map f
    }

  implicit def optFunctor[A, B]: Functor[Option[A], A, B, Option[B]] =
    new Functor[Option[A], A, B, Option[B]] {
      def map(opt: Option[A])(f: A => B): Option[B] =
        opt map f
    }

  implicit def functorParSeq[A, B]: Functor[ParSeq[A], A, B, ParSeq[B]] =
    new Functor[ParSeq[A], A, B, ParSeq[B]] {
      def map(ps: ParSeq[A])(f: A => B): ParSeq[B] =
        ps map f
    }

  import scala.collection.immutable.{ IndexedSeq => MIS }

  implicit def functorIndexedSeq[A, B]: Functor[MIS[A], A, B, MIS[B]] =
    new Functor[MIS[A], A, B, MIS[B]] {
      def map(is: MIS[A])(f: A => B): MIS[B] =
        is map f
    }

  implicit def Function1Functor[Function1[A, B], A, B, C]: Functor[A => B, B, C, A => C] =
    new Functor[A => B, B, C, A => C] {
      def map(ab: A => B)(bc: B => C): A => C =
        bc compose ab
    }

}