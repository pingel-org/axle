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

object PermuterTest {

	def main(args: Array[String]) {

		val elems = List("a", "b", "c")
		println("elems = " + elems)
        for( i <- 0 to elems.size ) {
            for( permutation <- new Permuter[String](elems, i) ) {
                println("p = " + permutation)
            }
        }
        
    }

}

class Permuter[E](objects: List[E], n: Int) extends Iterable[List[E]]
{
  
	if( n > objects.size ) {
		throw new IndexOutOfBoundsException()
	}

	def getN() = n
	
    def iterator() = new PermutionIterator[E](this)

    class PermutionIterator[InE](permuter: Permuter[InE]) extends Iterator[List[InE]]
    {
    	var remainders = new Array[Set[InE]](permuter.getN)
    	var iterators = new Array[Iterator[InE]](permuter.getN)
    	var tuple = new Array[InE](permuter.getN)
    	var i: Int

    	for( i <- 0 to (permuter.getN - 1) ) {
    		remainders.add(null)
    		iterators.add(null)
    		tuple.add(null)
    	}
            
    	if ( permuter.getN > 0 ) {
    		var firstRemainder = Set[InE]()
    		firstRemainder.addAll(permuter.objects)
    		remainders.set(0, firstRemainder)
    		iterators.set(0, firstRemainder.iterator())
    		tuple.set(0, iterators.get(i).next())
    	}
    	
    	for( i <- 1 to (permuter.getN - 1) ) {
    		setRemainder(i)
    		tuple.set(i, iterators.get(i).next())
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
            if( i > 0 ) {
                var r = Set[InE]()
                r.addAll(remainders.get(i-1))
                r.remove(tuple.get(i-1))
                remainders.set(i, r)
            }
            iterators.set(i, remainders.get(i).iterator())
        }
        
        def remove() = throw new UnsupportedOperationException()

        def hasNext() = tuple != null

        def incrementLastAvailable(i: Int) = {
            //System.out.println("incrementLastAvailable: i = " + i);
            if( i == -1 ) {
                return true
            }
            else if( iterators(i).hasNext() ) {
                tuple.set(i, iterators.get(i).next())
                return false
            }
            else {
                boolean touchedHead = incrementLastAvailable(i-1)
                setRemainder(i)
                tuple.set(i, iterators.get(i).next())
                return touchedHead
            }
        }
        
        def next() = {
            //System.out.println("next: remainders = " + remainders + ", tuple = " + tuple);
            if( tuple == null ) {
                throw new NoSuchElementException()
            }
            
            var result = new Array[InE]()
            result.addAll(tuple)
            
            if( incrementLastAvailable(permuter.n - 1) ) {
                tuple = null
            }
            
            result
        }
    }

}
