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
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * A PowerSet constructed with a collection with elements of type E can construct
 * an Iterator which enumerates all possible subsets (of type Collection<E>) of the
 * collection used to construct the PowerSet.
 * 
 * @author pingel
 *
 * @param <E> The type of elements in the Collection passed to the constructor.
 */

public class PowerSet<E> implements Iterable<Set<E>>
{
    Collection<E> all;

    public PowerSet(Collection<E> all)
    {
        this.all = all;
    }

    /**
     * @return      an iterator over elements of type Collection<E> which enumerates
     *              the PowerSet of the collection used in the constructor
     */

    public Iterator<Set<E>> iterator()
    {
        return new PowerSetIterator<E>(this);
    }

    class PowerSetIterator<InE> implements Iterator<Set<InE>>
    {
        PowerSet<InE> powerSet;
        List<InE> canonicalOrder = new ArrayList<InE>();
        List<InE> mask = new ArrayList<InE>();
        boolean hasNext = true;
        
        PowerSetIterator(PowerSet<InE> powerSet) {
            
            this.powerSet = powerSet;
            canonicalOrder.addAll(powerSet.all);
        }

        public void remove()
        {
            throw new UnsupportedOperationException();
        }

        private boolean allOnes()
        {
            for( InE bit : mask) {
                if( bit == null ) {
                    return false;
                }
            }
            return true;
        }

        private void increment()
        {
            int i=0;
            while( true ) {
                if( i < mask.size() ) {
                    InE bit = mask.get(i);
                    if( bit == null ) {
                        mask.set(i, canonicalOrder.get(i));
                        return;
                    }
                    else {
                        mask.set(i, null);
                        i++;
                    }
                }
                else {
                    mask.add(canonicalOrder.get(i));
                    return;
                }
            }
        }
        
        public boolean hasNext()
        {
            return hasNext;
        }
        
        public Set<InE> next()
        {
            
            Set<InE> result = new HashSet<InE>();
            result.addAll(mask);
            result.remove(null);

            hasNext = mask.size() < powerSet.all.size() || ! allOnes();

            if( hasNext ) {
                increment();
            }
            
            return result;

        }
        
    }
    
    /**
     * Sample code to demonstrate the use of PowerSet.
     * 
     * @param args
     */
    
    public static void main(String[] args)
    {

        List<String> elems = new ArrayList<String>();
        elems.add("a");
        elems.add("b");
        elems.add("c");
        elems.add("d");

        System.out.println("elems = " + elems);
        
        PowerSet<String> pset = new PowerSet<String>(elems);
        for( Set<String> set : pset ) {
            System.out.println(set);
        }

    }

}
