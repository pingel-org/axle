package org.pingel.angluin;

import java.util.Iterator;
import java.util.Vector;

import org.pingel.util.Stringer;

public class Expression {

    private Vector<Symbol> v = new Vector<Symbol>();

    public Expression() {
    }

    public Expression(Vector<Symbol> v) {
        this.v = v;
    }

    public Iterator<Symbol> getSymbolIterator() {
        return v.iterator();
    }

    public void addSymbol(Symbol s) {
        v.add(s);
    }

    public int length() {
        return v.size();
    }

    public Symbol getHead() {
        return v.elementAt(0);
    }

    public Expression getTail() {
        Vector<Symbol> v_copy = new Vector<Symbol>();
        v_copy.addAll(v);
        v_copy.removeElementAt(0);

        return new Expression(v_copy);
    }

    public boolean equals(Expression other) {
        // TODO !!!
        return false;
    }

    public String toString() {
        String result = "\"";
        result += Stringer.render(v, " ");
        result += "\"";

        return result;
    }

}
