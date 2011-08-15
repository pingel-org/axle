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

package org.pingel.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class CrossProduct<E> implements Iterable<List<E>>
{
	private List<Iterable<E>> iterables = new ArrayList<Iterable<E>>();

    public CrossProduct(List<? extends Iterable<E>> iterables)
    {
        this.iterables.addAll(iterables);
    }
    
	public CrossProduct(Iterable<E>... iterables)
	{
	    this.iterables = new ArrayList<Iterable<E>>();
        for( Iterable<E> it : iterables ) {
            this.iterables.add(it);
        }
	}
	
	public List<? extends Iterable<E>> getCollections()
	{
		return iterables;
	}

	public Iterator<List<E>> iterator()
	{
		return new CrossProductIterator<E>(this);
	}

	public static void main(String[] argv)
	{
	    List<String> v1 = new ArrayList<String>();
		List<String> v2 = new ArrayList<String>();
		List<String> v3 = new ArrayList<String>();
		
		v1.add("a");
		v1.add("b");
		v2.add("0");
		v2.add("1");
		v3.add("X");
		
		CrossProduct<String> cp = new CrossProduct<String>(v1, v2, v3, v2);
		
		Iterator<List<String>> it = cp.iterator();
		while( it.hasNext() ) {
		    List<String> tuple = it.next();
			System.out.println(tuple);
		}
		
	}
		
	
	class CrossProductIterator<InE> implements Iterator<List<InE>>
	{
        private CrossProduct<InE> cp;
		private List<Iterator<InE>> iterators;
		private List<InE> tuple;
		
		public CrossProductIterator(CrossProduct<InE> cp)
		{
            this.cp = cp;
            
			iterators = new ArrayList<Iterator<InE>>();
			tuple = new ArrayList<InE>();
			
			for(int i=0; i < cp.getCollections().size(); i++ ) {
				iterators.add(cp.getCollections().get(i).iterator());
				tuple.add(iterators.get(i).next());
			}
		}
		
		public void remove()
		{
			// I don't think there are any reasonable semantics
			// for "remove" since the "underlying collection"
			// is never actually instantiated
		    throw new UnsupportedOperationException();
        }
		
		public boolean hasNext()
		{
			return tuple != null;
		}
		
		boolean incrementFirstAvailable(int i)
		{
			if( i == iterators.size() ) {
				return true;
			}
			else if( iterators.get(i).hasNext() ) {
				tuple.set(i, iterators.get(i).next());
				return false;
			}
			else {
			    iterators.set(i, cp.iterables.get(i).iterator());
			    tuple.set(i, iterators.get(i).next());
				return incrementFirstAvailable(i+1);
			}
		}
		
		public List<InE> next()
		{
			if( tuple == null ) {
				throw new NoSuchElementException();    		
			}
			
			List<InE> result = new ArrayList<InE>();
			for(int i=0; i < tuple.size(); i++) {
				result.add(tuple.get(i));
			}
			
			if( incrementFirstAvailable(0) ) {
				tuple = null;
			}
			
			return result;
		}
	}

}
