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
      }
      else if (iterators(i).hasNext) {
        current(i) = iterators(i).next()
        return false
      }
      else {
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
