package axle

class PowerSet[E](all: Seq[E]) extends ℘[E](all)

/**
 * A ℘ (PowerSet) constructed with a collection with elements of type E can construct
 * an Iterator which enumerates all possible subsets (of type Collection<E>) of the
 * collection used to construct the PowerSet.
 *
 * @author Adam Pingel
 *
 * @param [E] The type of elements in the Collection passed to the constructor.
 */

import collection._

case class ℘[E](all: Seq[E]) extends Iterable[Set[E]] {

  def getAll = all

  /**
   * @return      an iterator over elements of type Collection<E> which enumerates
   *              the PowerSet of the collection used in the constructor
   */

  def iterator() = new PowerSetIterator[E](this)

  class PowerSetIterator[InE](powerSet: ℘[InE]) extends Iterator[Set[InE]] {

    val canonicalOrder = powerSet.getAll.toList

    val mask = mutable.ArrayBuffer[Option[InE]]()

    var hasNextValue = true

    def remove() = throw new UnsupportedOperationException()

    def increment(): Unit = {
      for (i <- 0 until mask.size) {
        if (mask(i) == None) {
          mask(i) = Some(canonicalOrder(i))
          return
        } else {
          mask(i) = None
        }
      }
      mask += Some(canonicalOrder(mask.size))
    }

    def hasNext() = hasNextValue

    def next() = {
      val result = mask.flatMap(x => x).toSet
      hasNextValue = (mask.size < powerSet.getAll.size) || mask.exists(_.isEmpty)
      if (hasNextValue) {
        increment()
      }
      result
    }

  }

}
