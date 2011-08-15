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
import java.util.List;


public class ListCrossProduct<E> extends CrossProduct<E>
{
    private List<List<E>> lists;
    private int[] modulos;

    public ListCrossProduct(List<E>... lists )
    {
        super((Iterable<E>[])lists);

        this.lists = new ArrayList<List<E>>();
        for( List<E> list : lists ) {
            this.lists.add(list);
        }
        
        setModulos();
    }

    public ListCrossProduct(List<List<E>> lists)
    {
        super(lists);
        
        this.lists = lists;
        
        setModulos();
    }
    
    private void setModulos()
    {
        modulos = new int[lists.size() + 1];
        modulos[lists.size()] = 1;
        for( int j = lists.size() - 1; j >= 0; j--) {
            modulos[j] = modulos[j+1] * lists.get(j).size();
        }
    }
    
    public int indexOf(List<E> objects)
    {
        if( objects.size() != lists.size() ) {
            System.err.println("ListCrossProduct: objects.size() != lists.size()");
            System.exit(1);
        }
        
        int i = 0;
        for(int j=0; j < lists.size(); j++ ) {
            List<E> l = lists.get(j);
            E elem = objects.get(j);
            int z = l.indexOf(elem);
            if( z == -1 ) {
                return -1;
            }
            i += z * modulos[j+1];
        }
//      System.out.println("answer = " + i);
        return i;

    }

    public List<E> get(int i) {

        List<E> result = new ArrayList<E>();
        for( int k=0; k < lists.size(); k++ ) {
            result.add(null);
        }

        for( int j=0; j < lists.size(); j++ ) {
            List<E> l = lists.get(j);
            int x = i / modulos[j+1];
            E o = l.get(x);
            result.set(j, o);
            i = i % modulos[j+1];
        }

        return result;
    }

    public int size()
    {
        return modulos[0];
    }

}
