/**
 * Copyright (c) 2008 Adam Pingel
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

package org.pingel.util

object CrossProductTest {

  	def main(args: Array[String]) {
  		val v1 = List("a", "b")
  		val v2 = List("0", "1")
  		val v3 = List("X")

		val cp = new CrossProduct[String](List(v1, v2, v3, v2))
		
		val it = cp.iterator()
		while( it.hasNext() ) {
		    val tuple = it.next()
			println(tuple)
		}
		
	}


}

class CrossProduct[E](iterables: List[_ <: Iterable[E]]) extends Iterable[List[E]]
{
	def getCollections() = iterables

	def iterator() = new CrossProductIterator[E](this)

	class CrossProductIterator[InE](cp: CrossProduct[InE]) extends Iterator[List[InE]]
	{
	  
		var iterators = new Array[Iterator[InE]](cp.getCollections().size)

		var tuple = new Array[InE](cp.getCollections().size)
				
		for( i <- 0 to (cp.getCollections().size - 1) ) {
			iterators(i) = cp.getCollections().get(i).iterator()
			tuple(i) = iterators(i).next()
		}

		def remove() = throw new UnsupportedOperationException()

		def hasNext() = tuple != null

		def incrementFirstAvailable(i: Int): Boolean = {

			if( i == iterators.size ) {
				return true
			}
			else if( iterators(i).hasNext() ) {
				tuple(i) = iterators(i).next()
				return false
			}
			else {
			    iterators(i) = cp.iterables(i).iterator()
			    tuple(i) = iterators(i).next()
				return incrementFirstAvailable(i+1)
			}
		}
		
		def next() = {
			if( tuple == null ) {
				throw new NoSuchElementException()
			}
			
			var result = new Array[InE](tuple.size)
			for( i <- 0 to (tuple.size - 1) ) {
				result(i) = tuple(i)
			}
			
			if( incrementFirstAvailable(0) ) {
				tuple = null
			}
			
			result
		}
	}

}
