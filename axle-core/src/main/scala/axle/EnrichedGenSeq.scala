package axle

import spire.math._
import spire.implicits._

import collection.GenSeq
import collection.immutable.TreeMap

case class EnrichedGenSeq[T](genSeq: GenSeq[T]) {

  def tally(): Map[T, Long] =
    genSeq.aggregate(Map.empty[T, Long].withDefaultValue(0L))(
      (m, x) => m + (x -> (m(x) + 1L)), _ + _)

  def orderedTally()(implicit o: Ordering[T]): TreeMap[T, Long] =
    new TreeMap[T, Long]() ++ tally()

}
