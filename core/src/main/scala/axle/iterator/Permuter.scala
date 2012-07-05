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

package axle.iterator

import collection._

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
    val remainders = mutable.ArrayBuffer[scala.collection.mutable.Set[InE]]()
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
