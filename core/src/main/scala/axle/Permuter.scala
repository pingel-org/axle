package axle

/**
 * Computes the permutations of length n from the given list
 * of objects.
 *
 * For example:
 *
 * new Permuter(List("a", "b", "c"), 2) will return 6 lists:
 *
 * List(b, a), List(b, c), List(a, b), List(a, c), List(c, b), List(c, a)
 *
 */

import collection._

case class Permuter[E](objects: List[E], n: Int) extends Iterable[List[E]] {

  if (n > objects.size) {
    throw new IndexOutOfBoundsException()
  }

  def getN = n

  def getObjects = objects

  def iterator() = new PermutionIterator[E](this)

  class PermutionIterator[InE](permuter: Permuter[InE]) extends Iterator[List[InE]] {
    val remainders = mutable.ArrayBuffer[immutable.Set[InE]]()
    val iterators = mutable.ArrayBuffer[Iterator[InE]]()
    var tuple = mutable.ArrayBuffer[InE]()

    if (permuter.getN > 0) {
      val firstRemainder = permuter.getObjects.toSet
      remainders += firstRemainder
      iterators += firstRemainder.iterator
      tuple += iterators(0).next()
    }

    for (i <- 1 until permuter.getN) {
      remainders += null
      iterators += null
      setRemainder(i)
      tuple += iterators(i).next()
    }

    def setRemainder(i: Int) = {
      if (i > 0) {
        remainders(i) = remainders(i - 1) - tuple(i - 1)
      }
      iterators(i) = remainders(i).iterator
    }

    def remove() = throw new UnsupportedOperationException()

    def hasNext() = tuple != null

    def incrementLastAvailable(i: Int): Boolean = {
      if (i == -1) {
        return true
      } else if (iterators(i).hasNext) {
        tuple(i) = iterators(i).next()
        return false
      } else {
        val touchedHead = incrementLastAvailable(i - 1)
        setRemainder(i)
        tuple(i) = iterators(i).next()
        return touchedHead
      }
    }

    def next() = {
      if (tuple == null) {
        throw new NoSuchElementException()
      }

      val result = tuple.toList
      if (incrementLastAvailable(permuter.getN - 1)) {
        tuple = null
      }
      result
    }
  }

}
