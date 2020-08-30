package axle

import scala.collection.GenSeq
import scala.collection.immutable.TreeMap

import cats.kernel.Order
import cats.Order.catsKernelOrderingForOrder
import spire.algebra.Ring
//import spire.implicits.MapCSemiring
//import spire.implicits.additiveSemigroupOps

case class EnrichedGenSeq[T](genSeq: GenSeq[T]) {

  def tally[N: Ring]: Map[T, N] = {
    val ring = Ring[N]
    import spire.implicits._
    Ring[Map[T, N]]
    genSeq.aggregate(Map.empty[T, N].withDefaultValue(ring.zero))(
      (m, x) => m + (x -> ring.plus(m(x), ring.one)),
      (p, q) => p + q)
  }

  def orderedTally[N: Ring](implicit o: Order[T]): TreeMap[T, N] = {
    new TreeMap[T, N]() ++ tally[N]
  }

}
