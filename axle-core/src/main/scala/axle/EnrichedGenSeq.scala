package axle

import scala.collection.GenSeq
import scala.collection.immutable.TreeMap

import cats.kernel.Order
import cats.Order.catsKernelOrderingForOrder
import spire.algebra.Ring
import spire.implicits.MapRng
import spire.implicits.additiveSemigroupOps

case class EnrichedGenSeq[T](genSeq: GenSeq[T]) {

  def tally[N: Ring]: Map[T, N] = {
    val ring = Ring[N]
    genSeq.aggregate(Map.empty[T, N].withDefaultValue(ring.zero))(
      (m, x) => m + (x -> ring.plus(m(x), ring.one)),
      _ + _)
  }

  def orderedTally[N: Ring](implicit o: Order[T]): TreeMap[T, N] = {
    new TreeMap[T, N]() ++ tally[N]
  }

}
