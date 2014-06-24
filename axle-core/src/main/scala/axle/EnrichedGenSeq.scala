package axle

import spire.math._
import spire.implicits._
import spire.algebra._

import collection.GenSeq
import collection.immutable.TreeMap

case class EnrichedGenSeq[T](genSeq: GenSeq[T]) {

  def tally[N: Ring]: Map[T, N] = {
    val ring = implicitly[Ring[N]]
    genSeq.aggregate(Map.empty[T, N].withDefaultValue(ring.zero))(
      (m, x) => m + (x -> ring.plus(m(x), ring.one)),
      _ + _)
  }

  def orderedTally[N: Ring](implicit o: Order[T]): TreeMap[T, N] = {
    implicit val ordering = Order.ordering
    new TreeMap[T, N]() ++ tally[N]
  }

}
