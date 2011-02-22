package org.pingel.angluin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Acceptor {

    private Set<AcceptorState> Q = new HashSet<AcceptorState>();

    private Set<AcceptorState> I = new HashSet<AcceptorState>();

    private Set<AcceptorState> F = new HashSet<AcceptorState>();

    private Map<AcceptorState, Map<Symbol, Set<AcceptorState>>> outArcs = new HashMap<AcceptorState, Map<Symbol, Set<AcceptorState>>>();

    private Map<AcceptorState, Map<Symbol, Set<AcceptorState>>> inArcs = new HashMap<AcceptorState, Map<Symbol, Set<AcceptorState>>>();

    public Acceptor() {
    }

    public void addState(AcceptorState p, boolean isInitial, boolean isFinal) {
        Q.add(p);

        if (isInitial)
            I.add(p);

        if (isFinal)
            F.add(p);

        outArcs.put(p, new HashMap<Symbol, Set<AcceptorState>>());
        inArcs.put(p, new HashMap<Symbol, Set<AcceptorState>>());
    }

    public void addTransition(AcceptorState from, Symbol symbol,
            AcceptorState to) {

        Map<Symbol, Set<AcceptorState>> symbol2outs = outArcs.get(symbol);
        Set<AcceptorState> outs = symbol2outs.get(symbol);

        if (outs == null) {
            outs = new HashSet<AcceptorState>();
            symbol2outs.put(symbol, outs);
        }
        outs.add(to);

        Map<Symbol, Set<AcceptorState>> symbol2ins = inArcs.get(symbol);
        Set<AcceptorState> ins = symbol2ins.get(symbol);

        if (ins == null) {
            ins = new HashSet<AcceptorState>();
            symbol2ins.put(symbol, ins);
        }

        ins.add(from);
    }

    public Set<AcceptorState> delta(AcceptorState state, Symbol symbol) {

        Map<Symbol, Set<AcceptorState>> symbol2outs = outArcs.get(state);

        Set<AcceptorState> outs = symbol2outs.get(symbol);

        return outs;
    }

    public Set<AcceptorState> delta(AcceptorState state, Expression exp) {
        Set<AcceptorState> result = new HashSet<AcceptorState>();

        if (exp == null) {
            result.add(state);
        } else {

            Symbol head = exp.getHead();
            Expression tail = exp.getTail();

            Set<AcceptorState> neighbors = delta(state, head);
            Iterator<AcceptorState> neighbor_it = neighbors.iterator();

            while (neighbor_it.hasNext()) {
                AcceptorState neighbor = neighbor_it.next();
                result.addAll(delta(neighbor, tail));
            }

        }

        return result;
    }

    public boolean isForwardDeterministic() {
        if (I.size() > 1) {
            return false;
        }

        Iterator<AcceptorState> state_it = Q.iterator();

        while (state_it.hasNext()) {

            AcceptorState state = state_it.next();

            Map<Symbol, Set<AcceptorState>> symbol2outs = outArcs.get(state);

            Set<Symbol> outSymbols = symbol2outs.keySet();
            Iterator<Symbol> outSymbolIt = outSymbols.iterator();

            while (outSymbolIt.hasNext()) {

                Symbol symbol = outSymbolIt.next();

                Set<AcceptorState> outs = symbol2outs.get(symbol);

                if (outs.size() > 1) {
                    return false;
                }
            }

        }

        return true;
    }

    public boolean isBackwardDeterministic() {
        if (F.size() > 1) {
            return false;
        }

        Iterator<AcceptorState> state_it = Q.iterator();

        while (state_it.hasNext()) {

            AcceptorState state = state_it.next();

            Map<Symbol, Set<AcceptorState>> symbol2ins = inArcs.get(state);

            Set<Symbol> inSymbols = symbol2ins.keySet();
            Iterator<Symbol> inSymbolIt = inSymbols.iterator();

            while (inSymbolIt.hasNext()) {

                Symbol symbol = inSymbolIt.next();

                Set<AcceptorState> ins = symbol2ins.get(symbol);

                if (ins.size() > 1) {
                    return false;
                }
            }

        }

        return true;
    }

    public boolean isZeroReversible() {
        return isForwardDeterministic() && isBackwardDeterministic();
    }

    public boolean isIsomorphicTo(Acceptor other) {
        // TODO !!!
        return false;
    }

    public boolean isSubacceptorOf(Acceptor other) {
        // TODO !!!
        return false;
    }

    public Acceptor induce(Set<AcceptorState> P) {
        // TODO !!!
        return null;
    }

}
