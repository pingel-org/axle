package axle

/**
 * Not yet implemented.
 *
 * I've heard told of a solution from Don Knuth
 *
 */

object Combinations {

  def apply[E](objects: Seq[E], n: Int): Combinations[E] = new Combinations[E](objects, n)
}

class Combinations[E](objects: Seq[E], n: Int) extends Iterable[Set[E]] {

  if (n > objects.size) {
    throw new IndexOutOfBoundsException()
  }

  def iterator() = new CombinationIterator[E](this)

  class CombinationIterator[InE](combiner: Combinations[InE]) extends Iterator[Set[InE]] {

    def remove() = throw new UnsupportedOperationException()

    def hasNext() = false

    def next(): Set[InE] = null

  }

}