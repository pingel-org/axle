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
    val remainders = mutable.ArrayBuffer[mutable.Set[InE]]()
    val iterators = mutable.ArrayBuffer[Iterator[InE]]()
    var tuple = mutable.ArrayBuffer[InE]()

    if (permuter.getN > 0) {
      val firstRemainder = mutable.Set[InE]()
      firstRemainder ++= permuter.getObjects
      remainders.append(firstRemainder)
      iterators.append(firstRemainder.iterator)
      tuple.append(iterators(0).next())
    }

    for (i <- 1 until permuter.getN) {
      remainders.append(null)
      iterators.append(null)
      setRemainder(i)
      tuple.append(iterators(i).next())
    }

    //        private void setTupleElement(int i)
    //        {
    // TODO an interesting case of software design here.  Saying "private tuple" isn't enough.
    // what I really want is to limit write access to tuple to this method.  In order to do that
    // I'd have to come up with an inner class (or some such mechanism).  I think there should
    // be better language support for this kind of thing.
    // Actually, I don't think an inner class would be enough... because the "private" data
    // member tuple would still be accessible here.
    // Does AOP solve this?  From what I understand, AOP would allow me to enforce the
    // "remainder setting always follows tuple element setting".  But I don't know about 
    // the visibility of such a statement.
    //        }

    def setRemainder(i: Int) = {
      //println("setRemainder: i = " + i)
      if (i > 0) {
        val r = mutable.Set[InE]()
        r ++= remainders(i - 1)
        r.remove(tuple(i - 1))
        remainders(i) = r
      }
      iterators(i) = remainders(i).iterator
    }

    def remove() = throw new UnsupportedOperationException()

    def hasNext() = tuple != null

    def incrementLastAvailable(i: Int): Boolean = {
      //println("incrementLastAvailable: i = " + i)
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
      // println("next: remainders = " + remainders + ", tuple = " + tuple)
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
