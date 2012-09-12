package axle

import collection._

class CrossProduct[E](iterables: Seq[_ <: Iterable[E]]) extends Iterable[List[E]] {

  def collections() = iterables

  def iterator() = new CrossProductIterator[E](this)

  class CrossProductIterator[InE](cp: CrossProduct[InE]) extends Iterator[List[InE]] {

    val iterators = mutable.ArrayBuffer[Iterator[InE]]()
    var current = mutable.ArrayBuffer[InE]()

    for (i <- 0 until cp.collections().size) {
      iterators.append(cp.collections()(i).iterator)
      current.append(iterators(i).next())
    }

    def remove() = throw new UnsupportedOperationException()

    def hasNext() = current != null

    def incrementFirstAvailable(i: Int): Boolean = {

      if (i == iterators.size) {
        return true
      } else if (iterators(i).hasNext) {
        current(i) = iterators(i).next()
        return false
      } else {
        iterators(i) = cp.collections()(i).iterator
        current(i) = iterators(i).next()
        return incrementFirstAvailable(i + 1)
      }
    }

    def next() = {
      if (current == null) {
        throw new NoSuchElementException()
      }

      val result = current.toList
      if (incrementFirstAvailable(0)) {
        current = null
      }
      result
    }
  }

}
