package axle

import collection._

case class EnrichedGenSet[T](s: GenSet[T]) {

  def ∪(other: GenSet[T]) = s.union(other)

  def ∩(other: GenSet[T]) = s.intersect(other)
}
