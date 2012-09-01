
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

import collection._
import math.{ exp, log }

package object axle {

  case class EnrichedGenTraversable[+T](gt: GenTraversable[T]) {

    def Σ(f: T => Double) = gt.aggregate(0.0)(_ + f(_), _ + _)

    def Sigma(f: T => Double) = Σ(f)

    def Πx(f: T => Double): Double = exp(gt.map(x => log(f(x))).sum) // TODO: use aggregate for sum?

    def Π(f: T => (() => Double)): Double = gt.aggregate(1.0)((a, b) => a * f(b)(), (x, y) => x * y)

    def Pi(f: T => (() => Double)) = Π(f)

    def ∀(p: T => Boolean) = gt.forall(p)

    def ∃(p: T => Boolean) = gt.exists(p)

    def doubles(): GenTraversable[(T, T)] = for (x <- gt; y <- gt) yield (x, y)

    def triples(): GenTraversable[(T, T, T)] = for (x <- gt; y <- gt; z <- gt) yield (x, y, z)

    def ⨯[S](right: GenTraversable[S]) = for (x <- gt; y <- right) yield (x, y)

  }

  implicit def enrichGenTraversable[T](gt: GenTraversable[T]) = EnrichedGenTraversable(gt)

  case class EnrichedByteArray(barr: Array[Byte]) {
    def ⊕(other: Array[Byte]): Array[Byte] = barr.zip(other).map(lr => (lr._1 ^ lr._2).toByte).toArray
  }

  implicit def enrichByteArray(barr: Array[Byte]) = EnrichedByteArray(barr)

  case class EnrichedArray[T](arr: Array[T]) {

    def apply(range: Range) = {
      assert(range.step == 1)
      arr.slice(range.start, range.end)
    }
  }

  implicit def enrichArray[T](arr: Array[T]) = EnrichedArray(arr)

  case class EnrichedBoolean(b: Boolean) {

    def ¬:() = !b

    def ∧(other: Boolean) = b && other
    def and(other: Boolean) = b && other

    def ∨(other: Boolean) = b || other
    def or(other: Boolean) = b || other

    def ⊃(other: Boolean) = (!b) || other
    def implies(other: Boolean) = (!b) || other

    def ⊕(other: Boolean) = ((!b) && (!other)) || (b && other)
    def ⊻(other: Boolean) = ((!b) && (!other)) || (b && other)
  }

  implicit def enrichBoolean(b: Boolean) = EnrichedBoolean(b)

  case class Combiner[E](objects: Seq[E], n: Int) extends Iterable[Set[E]] {

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

  class ListCrossProduct[E](lists: Seq[List[E]]) extends CrossProduct[E](lists) {

    val modulos = new Array[Int](lists.size + 1)
    modulos(lists.size) = 1
    for (j <- (lists.size - 1).to(0, -1)) {
      modulos(j) = modulos(j + 1) * lists(j).size
    }

    def indexOf(objects: List[E]): Int = {
      if (objects.size != lists.size) {
        throw new Exception("ListCrossProduct: objects.size() != lists.size()")
      }
      var i = 0
      for (j <- 0 until lists.size) {
        val z = lists(j).indexOf(objects(j))
        if (z == -1) {
          return -1
        }
        i += z * modulos(j + 1)
      }
      i
    }

    def apply(i: Int) = {
      var c = i
      val result = mutable.ArrayBuffer[E]()
      for (j <- 0 until lists.size) {
        result.append(lists(j)(c / modulos(j + 1)))
        c = c % modulos(j + 1)
      }
      result.toList
    }

    override def size() = modulos(0)

  }

  class CrossProduct[E](iterables: Seq[_ <: Iterable[E]]) extends Iterable[List[E]] {
    def getCollections() = iterables

    def iterator() = new CrossProductIterator[E](this)

    class CrossProductIterator[InE](cp: CrossProduct[InE]) extends Iterator[List[InE]] {

      val iterators = mutable.ArrayBuffer[Iterator[InE]]()
      var current = mutable.ArrayBuffer[InE]()

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
        //System.out.println("setRemainder: i = " + i);
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
        // println("next: remainders = " + remainders + ", tuple = " + tuple);
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

      def allOnes(): Boolean = mask.forall(_.isDefined)

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
        val result = mask.flatMap(x => x).toSet
        hasNextValue = mask.size < powerSet.getAll.size || !allOnes()
        if (hasNextValue) {
          increment()
        }
        result
      }

    }

  }

}
