package axle.algebra

import scala.annotation.implicitNotFound
import scala.reflect.ClassTag
import scala.collection.parallel.immutable.ParSeq

@implicitNotFound("No member of typeclass Functor found for type ${F}")
trait Functor[F[_]] {
  def map[A, B: ClassTag](xs: F[A])(f: A => B): F[B]
}

object Functor {

  def apply[F[_]: Functor]: Functor[F] = implicitly[Functor[F]]

  implicit def functorSeq =
    new Functor[Seq] {
      def map[A, B: ClassTag](seq: Seq[A])(f: A => B): Seq[B] =
        seq map f
    }

  implicit def ListFunctor: Functor[List] =
    new Functor[List] {
      def map[A, B: ClassTag](list: List[A])(f: A => B) =
        list map f
    }

  implicit def IndexedSeqFunctor: Functor[IndexedSeq] =
    new Functor[IndexedSeq] {
      def map[A, B: ClassTag](is: IndexedSeq[A])(f: A => B) =
        is map f
    }

  implicit def OptFunctor: Functor[Option] =
    new Functor[Option] {
      def map[A, B: ClassTag](opt: Option[A])(f: A => B) =
        opt map f
    }

  implicit def Function1Functor[A]: Functor[({ type λ[α] = (A) => α })#λ] =
    new Functor[({ type λ[α] = (A) => α })#λ] {
      def map[B, C: ClassTag](fn: A => B)(f: B => C) =
        f compose fn
    }

  implicit def functorParSeq: Functor[ParSeq] =
    new Functor[ParSeq] {
      def map[A, B: ClassTag](ps: ParSeq[A])(f: A => B): ParSeq[B] =
        ps map f
    }

  implicit def functorIndexedSeq: Functor[scala.collection.immutable.IndexedSeq] =
    new Functor[scala.collection.immutable.IndexedSeq] {
      def map[A, B: ClassTag](is: scala.collection.immutable.IndexedSeq[A])(f: A => B): scala.collection.immutable.IndexedSeq[B] =
        is map f
    }

}