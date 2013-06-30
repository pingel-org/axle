package axle

import collection.GenSeq
import spire.implicits._

case class EnrichedGenSeq[T](genSeq: GenSeq[T]) {

  implicit val longsemi = axle.algebra.Semigroups.LongSemigroup // TODO remove this

  def countMap(): Map[T, Long] =
    genSeq.aggregate(Map.empty[T, Long].withDefaultValue(0L))(
      (m, x) => m + (x -> (m(x) + 1)), _ |+| _)

}