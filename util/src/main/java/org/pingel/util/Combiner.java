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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/*
 * NOTE: COMBINER IS NOT YET IMPLEMENTED
 */

public class Combiner<E> implements Iterable<Set<E>>
{
    Collection<E> objects;
    int n;
    
    public Combiner(Collection<E> objects, int n)
    {
        this.objects = objects;
        this.n = n;

        if( n > objects.size() ) {
            throw new IndexOutOfBoundsException();
        }
    }

    public Iterator<Set<E>> iterator()
    {
        return new CombinationIterator<E>(this);
    }

    class CombinationIterator<InE> implements Iterator<Set<InE>>
    {
        Combiner<InE> combiner;

        CombinationIterator(Combiner<InE> combiner)
        {
            this.combiner = combiner;
        }

        public void remove()
        {
            throw new UnsupportedOperationException();
        }
        
        public boolean hasNext()
        {
        	return false;
        }
        
        public Set<InE> next()
        {
        	return null;
        }
    }


    public static void main(String[] args) {

        List<String> elems = new ArrayList<String>();
        elems.add("a");
        elems.add("b");
        elems.add("c");

        System.out.println("elems = " + elems);

        for( int i=0; i <= elems.size(); i++ ) {
            Combiner<String> combiner = new Combiner<String>(elems, i);
            for( Set<String> combination : combiner ) {
                System.out.println("p = " + combination);
            }
        }
        
    }

}
