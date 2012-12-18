package axle

import collection._
import util.Random.nextInt

object EnrichedIndexedSeq {

  def apply[T](is: IndexedSeq[T]) = new EnrichedIndexedSeq(is)
}

class EnrichedIndexedSeq[T](is: IndexedSeq[T]) {

  // TODO: apply is very similar to EnrichedMutableBuffer.apply(Range)

  def apply(range: Range): IndexedSeq[T] = {
    assert(range.step == 1)
    if (range.isEmpty) {
      List[T]().toIndexedSeq
    } else {
      is.slice(range.start, range.last + 1)
    }
  }

  def random(): T = is(nextInt(is.size))

  def powerset(): IndexedPowerSet[T] = IndexedPowerSet(is)

  def ℘(): IndexedPowerSet[T] = IndexedPowerSet(is)

  def permutations(r: Int): Permutations[T] = Permutations(is, r)

  def combinations(r: Int): Combinations[T] = Combinations(is, r)
}