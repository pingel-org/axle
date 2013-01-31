package axle

import collection._

class CrossProduct[E](iterables: Seq[_ <: Iterable[E]]) extends Iterable[List[E]] {

  def collections() = iterables

  def iterator() = new CrossProductIterator[E](this)

  class CrossProductIterator[InE](cp: CrossProduct[InE]) extends Iterator[List[InE]] {

    val iterators = mutable.ArrayBuffer[Iterator[InE]]()
    var current: Option[mutable.ArrayBuffer[InE]] = Some(mutable.ArrayBuffer[InE]())

    for (i <- 0 until cp.collections().size) {
      iterators.append(cp.collections()(i).iterator)
      current.get.append(iterators(i).next()) // TODO .get
    }

    def remove() = throw new UnsupportedOperationException()

    def hasNext() = current.isDefined

    def incrementFirstAvailable(i: Int): Boolean = {

      if (i == iterators.size) {
        true
      } else if (iterators(i).hasNext) {
        current.get.update(i, iterators(i).next())
        false
      } else {
        iterators(i) = cp.collections()(i).iterator
        current.get.update(i, iterators(i).next())
        incrementFirstAvailable(i + 1)
      }
    }

    def next() = {
      if (current.isEmpty) {
        throw new NoSuchElementException()
      }

      val result = current.get.toList // TODO .get
      if (incrementFirstAvailable(0)) {
        current = None.asInstanceOf[Option[mutable.ArrayBuffer[InE]]]
      }
      result
    }
  }

}
