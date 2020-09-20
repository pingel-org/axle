package axle.algebra

import cats.Functor

package object functors {

  implicit val functorIndexedSeq: Functor[IndexedSeq] =
    new Functor[IndexedSeq] {
      def map[A, B](as: IndexedSeq[A])(f: A => B): IndexedSeq[B] =
        as.map(f)
    }

  implicit val functorSeq: Functor[Seq] =
    new Functor[Seq] {
      def map[A, B](as: Seq[A])(f: A => B): Seq[B] =
        as.map(f)
    }

}
