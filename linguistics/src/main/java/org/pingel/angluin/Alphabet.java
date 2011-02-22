package org.pingel.angluin;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Alphabet {

    private Set<Symbol> symbols;

    public Alphabet() {
        symbols = new HashSet<Symbol>();
    }

    public void addSymbol(Symbol m) {
        symbols.add(m);
    }

    public Iterator<Symbol> iterator() {
        return symbols.iterator();
    }

}
