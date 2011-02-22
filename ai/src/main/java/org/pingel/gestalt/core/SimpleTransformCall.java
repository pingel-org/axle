package org.pingel.gestalt.core;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class SimpleTransformCall extends CallGraph
{
	private SimpleTransform transform;

    public SimpleTransformCall(int id, History history, Lexicon lexicon, SimpleTransform transform, TransformEdge macro)
    {
    	super(id, history, lexicon, transform, macro);

    	this.transform = transform;
    	// TODO assert: out.variables equals bindings.keySet
    }

    public Map<Name, Form> replacements()
    {
		GLogger.global.entering("SimpleTransformCall", "replacements");

		Map<Name, Form> replacements = new TreeMap<Name, Form>();
		Iterator<Name> map_it = transform.map.keySet().iterator();
		while( map_it.hasNext() ) {

		    Name mi = map_it.next();
		    Name variable = transform.map.get(mi);
		    Form replacement = unifier.get(mi);

		    {
				// too strict !!
				Form already = replacements.get(variable);
				if( already != null ) {
				    GLogger.global.info("SimpleSystmCall.replacements variable is already bound to " + already.toString());
			    
					// TODO Shouldn't this have already been done in the Call superclass ??
	
					// matched = false;
					hasNext = false;
					return null;
				}
		    }

		    replacements.put(variable, replacement);
		}

        return replacements;
    }

    public void next(History history, Lexicon lexicon)
    {
		GLogger.global.entering("SimpleSystmCall", "next");

        Map<Name, Form> replacements = replacements();
        if( replacements == null ) {
            return;
        }

		Form resultForm = lexicon.getForm(transform.outName).duplicateAndReplace(replacements);
	
		CallVertex resultVertex = getGraph().addVertex(new CallVertex(history.nextVertexId(), transform.exitNode, resultForm));

		GLogger.global.info("Transform result is " + resultForm.toString());

		getGraph().addEdge(new CallEdge(history.nextEdgeId(), start, resultVertex, macroEdge));

		outputs.add(resultVertex);
		
		hasNext = false;

        GLogger.global.exiting("SimpleTransformCall", "next");
    }

    public double cost()
    {
        return transform.cost;
    }

}
