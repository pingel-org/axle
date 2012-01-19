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

package org.pingel.axle.iterator

import scala.collection._

class PowerSet[E](all: Collection[E]) extends ℘[E](all)

/**
 * A ℘ (PowerSet) constructed with a collection with elements of type E can construct
 * an Iterator which enumerates all possible subsets (of type Collection<E>) of the
 * collection used to construct the PowerSet.
 *
 * @author Adam Pingel
 *
 * @param [E] The type of elements in the Collection passed to the constructor.
 */

class ℘[E](all: Collection[E]) extends Iterable[Set[E]] {

  def getAll = all

  /**
   * @return      an iterator over elements of type Collection<E> which enumerates
   *              the PowerSet of the collection used in the constructor
   */

  def iterator() = new PowerSetIterator[E](this)

  class PowerSetIterator[InE](powerSet: ℘[InE]) extends Iterator[Set[InE]] {

    val canonicalOrder = powerSet.getAll.toList

    var mask = mutable.ArrayBuffer[Option[InE]]()

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


object PowerSetTest {

  def main(args: Array[String]) {
    
    val elems = List("a", "b", "c", "d")
    println("elems = " + elems)
    for (set <- new PowerSet[String](elems)) {
      println(set)
    }
    
  }
  
}
