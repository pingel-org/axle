package org.pingel.gestalt.core;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.pingel.util.Printable;
import org.pingel.util.PrintableStringBuffer;

public class History {

    private int nextEdgeId = 0;

    private int nextVertexId = 0;

    private int nextCallId = 0;

    private Set<CallGraph> calls = new HashSet<CallGraph>();

    public History() {
    }

    public int nextEdgeId() {
        return nextEdgeId++;
    }

    public int nextVertexId() {
        return nextVertexId++;
    }

    public int nextCallId() {
        return nextCallId++;
    }

    public void addCall(CallGraph call) {
        calls.add(call);
    }

    public void remove(CallGraph call) {
        calls.remove(call);
    }

    public Set<CallGraph> getCalls() {
        return calls;
    }

    public Set<CallGraph> callVerticesByTransformVertex(TransformVertex tv) {
        // TODO
        return null;
    }

    public void printTo(Printable out, Lexicon lexicon) {
        Iterator<CallGraph> callIt = calls.iterator();
        while (callIt.hasNext()) {

            CallGraph call = callIt.next();

            out.println(call.getId() + " " + lexicon.getNameOf(call.transform));
            out.println();
            call.printNetworkTo(out, 0);
            out.println();
        }
    }

    public String toString(Lexicon lexicon) {
        Printable psb = new PrintableStringBuffer(new StringBuffer());

        this.printTo(psb, lexicon);

        return psb.toString();
    }

    public void run(String formName, String transformName, Lexicon lexicon) {
        Form form = lexicon.getForm(new Name(formName));
        Transform transform = lexicon.getTransform(new Name(transformName));
        if (transform == null) {
            GLogger.global.severe("Couldn't find transform with name " + transformName);
            System.exit(1);
        }

        CallGraph call = transform.constructCall(nextCallId(), this, lexicon,
                null);
        CallVertex cv = new CallVertex(nextVertexId(), transform.start, form);
        call.unify(this, cv);

        while (call.hasNext()) {
            GLogger.global.info("top-level call\n");
            GLogger.global.info(call.toString() + "\n");
            call.next(this, lexicon);
        }

    }

}
