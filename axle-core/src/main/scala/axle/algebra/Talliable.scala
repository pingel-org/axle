package axle.algebra

import scala.annotation.implicitNotFound
import scala.collection.immutable.TreeMap

import spire.algebra.CRing

import cats.kernel.Order
import cats.Order.catsKernelOrderingForOrder


@implicitNotFound("Witness not found for Talliable[${F}]")
trait Talliable[F[_]] {

  def tally[A, N](xs: F[A])(implicit ring: CRing[N]): Map[A, N]

  def orderedTally[A: Order, N: CRing](xs: F[A]): TreeMap[A, N] = {
    new TreeMap[A, N]() ++ tally[A, N](xs)
  }

}

object Talliable {

  implicit val tallySeq =
    new Talliable[Seq] {

      def tally[A, N](xs: Seq[A])(implicit ring: CRing[N]): Map[A, N] = {
        xs.foldLeft(Map.empty[A, N].withDefaultValue(ring.zero))(
          (m, x) => m + (x -> ring.plus(m(x), ring.one)))
      }
    }

  implicit val tallyList =
    new Talliable[List] {

      def tally[A, N](xs: List[A])(implicit ring: CRing[N]): Map[A, N] = {
        xs.foldLeft(Map.empty[A, N].withDefaultValue(ring.zero))(
          (m, x) => m + (x -> ring.plus(m(x), ring.one)))
      }
    }

  implicit val tallyIterable =
    new Talliable[Iterable] {
      def tally[A, N](xs: Iterable[A])(implicit ring: CRing[N]): Map[A, N] = {
        xs.foldLeft(Map.empty[A, N].withDefaultValue(ring.zero))(
          (m, x) => m + (x -> ring.plus(m.get(x).getOrElse(ring.zero), ring.one) )
        )
      }
    }

}
