/**
 * Copyright (c) 2012 Adam Pingel
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package org.pingel.axle.iterator {

  import scala.collection._

  case class Lister()

  class ListCrossProduct[E](lists: List[List[E]]) extends CrossProduct[E](lists) {

    var modulos = new Array[Int](lists.size + 1)
    modulos(lists.size) = 1
    var j = lists.size - 1
    while (j >= 0) {
      modulos(j) = modulos(j + 1) * lists(j).size
      j -= 1
    }

    def indexOf(objects: List[E]): Int = {

      if (objects.size != lists.size) {
        throw new Exception("ListCrossProduct: objects.size() != lists.size()")
      }

      var i = 0
      for (j <- 0 until lists.size) {
        val l = lists(j)
        val elem = objects(j)
        val z = l.indexOf(elem)
        if (z == -1) {
          return -1
        }
        i += z * modulos(j + 1)
      }
      // println("answer = " + i);
      i
    }

    def get(i: Int) = {
      var c = i
      //        var result = lists.map({x => null}).toList.toArray // Array[E] or List[E]?
      var result = scala.collection.mutable.ArrayBuffer[E]()
      for (j <- 0 until lists.size) {
        result.append(lists(j)(c / modulos(j + 1)))
        c = c % modulos(j + 1)
      }
      result.toList
    }

    override def size() = modulos(0)

  }

  object CrossProductTest {

    def main(args: Array[String]) {
      val v1 = List("a", "b")
      val v2 = List("0", "1")
      val v3 = List("X")

      val cp = new CrossProduct[String](List(v1, v2, v3, v2))

      val it = cp.iterator()
      while (it.hasNext()) {
        val tuple = it.next()
        println(tuple)
      }

    }

  }

  class CrossProduct[E](iterables: List[_ <: Iterable[E]]) extends Iterable[List[E]] {
    def getCollections() = iterables

    def iterator() = new CrossProductIterator[E](this)

    class CrossProductIterator[InE](cp: CrossProduct[InE]) extends Iterator[List[InE]] {

      var iterators = scala.collection.mutable.ArrayBuffer[Iterator[InE]]()
      var current = scala.collection.mutable.ArrayBuffer[InE]()

      for (i <- 0 until cp.getCollections().size) {
        iterators.append(cp.getCollections()(i).iterator)
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
          iterators(i) = cp.getCollections()(i).iterator
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

  object PowerSetTest {

    def main(args: Array[String]) {

      val elems = List("a", "b", "c", "d")
      println("elems = " + elems)
      for (set <- new PowerSet[String](elems)) {
        println(set)
      }

    }

  }

  /**
   * A PowerSet constructed with a collection with elements of type E can construct
   * an Iterator which enumerates all possible subsets (of type Collection<E>) of the
   * collection used to construct the PowerSet.
   *
   * @author pingel
   *
   * @param [E] The type of elements in the Collection passed to the constructor.
   */

  class PowerSet[E](all: Collection[E]) extends Iterable[Set[E]] {
    def getAll = all

    /**
     * @return      an iterator over elements of type Collection<E> which enumerates
     *              the PowerSet of the collection used in the constructor
     */

    def iterator() = new PowerSetIterator[E](this)

    class PowerSetIterator[InE](powerSet: PowerSet[InE]) extends Iterator[Set[InE]] {
      val canonicalOrder = powerSet.getAll.toList

      var mask = scala.collection.mutable.ArrayBuffer[Option[InE]]()

      var hasNextValue = true

      def remove() = throw new UnsupportedOperationException()

      def allOnes(): Boolean = {
        for (bit <- mask) {
          if (bit == None) {
            return false
          }
        }
        true
      }

      def increment(): Unit = {
        var i = 0
        while (true) {
          if (i < mask.size) {
            val bit = mask(i)
            if (bit == None) {
              mask(i) = Some(canonicalOrder(i))
              return
            } else {
              mask(i) = None
              i += 1
            }
          } else {
            mask.append(Some(canonicalOrder(i)))
            return
          }
        }
      }

      def hasNext() = hasNextValue

      def next() = {

        val result = mask.flatMap({ x => x })

        hasNextValue = mask.size < powerSet.getAll.size || !allOnes()
        if (hasNextValue) {
          increment()
        }

        result.toSet

      }

    }

  }

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

  class Permuter[E](objects: List[E], n: Int) extends Iterable[List[E]] {

    if (n > objects.size) {
      throw new IndexOutOfBoundsException()
    }

    def getN = n

    def getObjects = objects

    def iterator() = new PermutionIterator[E](this)

    class PermutionIterator[InE](permuter: Permuter[InE]) extends Iterator[List[InE]] {
      var remainders = scala.collection.mutable.ArrayBuffer[scala.collection.mutable.Set[InE]]()

      var iterators = scala.collection.mutable.ArrayBuffer[Iterator[InE]]()

      var tuple = scala.collection.mutable.ArrayBuffer[InE]()

      //    	var i: Int

      if (permuter.getN > 0) {
        var firstRemainder = scala.collection.mutable.Set[InE]()
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
        //System.out.println("setRemainder: i = " + i);
        if (i > 0) {
          var r = scala.collection.mutable.Set[InE]()
          r ++= remainders(i - 1)
          r.remove(tuple(i - 1))
          remainders(i) = r
        }
        iterators(i) = remainders(i).iterator
      }

      def remove() = throw new UnsupportedOperationException()

      def hasNext() = tuple != null

      def incrementLastAvailable(i: Int): Boolean = {
        //System.out.println("incrementLastAvailable: i = " + i);
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
        //System.out.println("next: remainders = " + remainders + ", tuple = " + tuple);
        if (tuple == null) {
          throw new NoSuchElementException()
        }

        var result = tuple.toList
        if (incrementLastAvailable(permuter.getN - 1)) {
          tuple = null
        }
        result
      }
    }

  }

  object CombinerTest {

    def main(args: Array[String]) {
      val elems = List("a", "b", "c")
      println("elems = " + elems)
      for (i <- 0 to elems.size) {
        for (combination <- new Combiner[String](elems, i)) {
          System.out.println("p = " + combination);
        }
      }
    }

  }

  class Combiner[E](objects: Collection[E], n: Int) extends Iterable[Set[E]] {

    if (n > objects.size) {
      throw new IndexOutOfBoundsException()
    }

    def iterator() = new CombinationIterator[E](this)

    class CombinationIterator[InE](combiner: Combiner[InE]) extends Iterator[Set[InE]] {

      def remove() = throw new UnsupportedOperationException()

      def hasNext() = false

      def next(): Set[InE] = null

    }

  }

}
