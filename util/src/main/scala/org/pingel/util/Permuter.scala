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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

public class Permuter<E> implements Iterable<List<E>>
{
    List<E> objects;
    int n;

    public Permuter(List<E> objects, int n)
    {
        this.objects = objects;
        this.n = n;
        
        if( n > objects.size() ) {
            throw new IndexOutOfBoundsException();
        }
    }

    public Iterator<List<E>> iterator()
    {
        return new PermutionIterator<E>(this);
    }

    class PermutionIterator<InE> implements Iterator<List<InE>>
    {
        private Permuter<InE> permuter;
        private List<Set<InE>> remainders;
        private List<Iterator<InE>> iterators;
        private List<InE> tuple;
        private int i;
        
        PermutionIterator(Permuter<InE> permuter)
        {
            this.permuter = permuter;

            remainders = new ArrayList<Set<InE>>(permuter.n);
            iterators = new ArrayList<Iterator<InE>>(permuter.n);
            tuple = new ArrayList<InE>(permuter.n);

            for( int i=0; i < permuter.n; i++ ) {
                remainders.add(null);
                iterators.add(null);
                tuple.add(null);
            }
            
            if( permuter.n > 0 ) {
                Set<InE> firstRemainder = new HashSet<InE>();
                firstRemainder.addAll(permuter.objects);
                remainders.set(0, firstRemainder);
                iterators.set(0, firstRemainder.iterator());
                tuple.set(0, iterators.get(i).next());
            }
            
            for( int i=1; i < permuter.n; i++ ) {
                setRemainder(i);
                tuple.set(i, iterators.get(i).next());
            }
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
        
        private void setRemainder(int i)
        {
            //System.out.println("setRemainder: i = " + i);
            if( i > 0 ) {
                Set<InE> r = new HashSet<InE>();
                r.addAll(remainders.get(i-1));
                r.remove(tuple.get(i-1));
                remainders.set(i, r);
            }
            iterators.set(i, remainders.get(i).iterator());
        }
        
        public void remove()
        {
            throw new UnsupportedOperationException();
        }
        
        public boolean hasNext()
        {
            return tuple != null;
        }

        boolean incrementLastAvailable(int i)
        {
            //System.out.println("incrementLastAvailable: i = " + i);
            if( i == -1 ) {
                return true;
            }
            else if( iterators.get(i).hasNext() ) {
                tuple.set(i, iterators.get(i).next());
                return false;
            }
            else {
                boolean touchedHead = incrementLastAvailable(i-1);
                setRemainder(i);
                tuple.set(i, iterators.get(i).next());
                return touchedHead;
            }
        }
        
        public List<InE> next()
        {
            //System.out.println("next: remainders = " + remainders + ", tuple = " + tuple);
            if( tuple == null ) {
                throw new NoSuchElementException();         
            }
            
            List<InE> result = new ArrayList<InE>();
            result.addAll(tuple);
            
            if( incrementLastAvailable(permuter.n - 1) ) {
                tuple = null;
            }
            
            return result;

        }
    }

    public static void main(String[] args) {

        List<String> elems = new ArrayList<String>();
        elems.add("a");
        elems.add("b");
        elems.add("c");

        System.out.println("elems = " + elems);

        for( int i=0; i <= elems.size(); i++ ) {
            Permuter<String> permuter = new Permuter<String>(elems, i);
            for( List<String> permutation : permuter ) {
                System.out.println("p = " + permutation);
            }
        }
        
    }

}
