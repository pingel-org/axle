package org.pingel.gestalt.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ComplexTransformCall extends CallGraph
{
    private Set<CallGraph> activeCalls = new HashSet<CallGraph>();
    private Map<CallGraph, CallVertex> call2input = new HashMap<CallGraph, CallVertex>();
    private boolean initialized = false;

    ComplexTransform transform;
    
    public ComplexTransformCall(int id, History history, Lexicon lexicon,
    		ComplexTransform transform, TransformEdge macro)
    {
    	super(id, history, lexicon, transform, macro);
    	
    	this.transform = transform;
    }
    
    private CallGraph cheapestCall()
    {
        CallGraph cheapest = null;
        double cheapest_cost = 999999;
        
		GLogger.global.info("ComplexTransformCall.cheapestCall: activeCalls.size() = " + activeCalls.size());
        
		for( CallGraph call : activeCalls ) {
            
            GLogger.global.info("cheapestCall in loop");
            
            double current_cost = call.cost();
            
            if( call == null || current_cost < cheapest_cost ) {
                cheapest_cost = current_cost;
                cheapest = call;
            }
        }

        GLogger.global.info("cheapestCall done");
        
        return cheapest;
    }

    private double networkCost(CallVertex cv)
    {
        double running_total = 0;
        
        Set<CallEdge> outputEdges = getGraph().outputEdgesOf(cv);
        for( CallEdge outputEdge : outputEdges ) {
            running_total += networkCost(outputEdge.getDest());
        }
        
        return 1 + running_total;
    }

    
    public double cost() 
    {
        if( start == null ) {
            return 0;
        }
        else {
            return networkCost(start);
        }
    }

   
    public void createNextCalls(History history,
            Lexicon lexicon,
            CallVertex state)
    {
        GLogger.global.entering("ComplexTransformCall", "createNextCalls: state = " + state.toString());
        GLogger.global.fine("state.pnode.isExit() = " + state.getTransformVertex().isExit());

        GLogger.global.fine("prior to state.pnode.createNextCalls, there are " +
                activeCalls.size() + " calls in active_calls");

        if( state.getTransformVertex().isExit() ) {
            outputs.add(state);
        }

        Set<TransformEdge> outputEdges = transform.getGraph().outputEdgesOf(state.getTransformVertex());
            
        for( TransformEdge nextEdge : outputEdges ) {
            
            GLogger.global.info("found node successor with system " + nextEdge.getDest().name );
            
            Transform nextTransform = lexicon.getTransform(nextEdge.transformName);
            
            CallVertex traversedState =
                new CallVertex(history.nextVertexId(),
                        nextTransform.start,
                        state.getForm().traverse(nextEdge.traversal));
            
            CallGraph nextCall =
                nextTransform.constructCall(history.nextCallId(), history, lexicon, nextEdge);
            nextCall.unify(history, traversedState);
            
            call2input.put(nextCall, state);
            
            activeCalls.add(nextCall);

            GLogger.global.fine("after state.pnode.createNextCalls, there are " +
                    activeCalls.size() + " calls in active_calls");
}
    }
    
    public void next(History history, Lexicon lexicon)
    {
		GLogger.global.entering("ComplexTransformCall", "next");

		if( ! initialized ) {
			createNextCalls(history, lexicon, start);
			initialized = true;
		}
    
        CallGraph cg = cheapestCall();
        if( cg == null ) {
            hasNext = false;
		    GLogger.global.fine("ComplexTransformCall.next: call == null. end");
            return;
        }

		GLogger.global.info("ComplexTransformCall's call is " + cg.toString());
        
        if( cg.hasNext() ) {
            cg.next(history, lexicon);
        }
        else {
          
            activeCalls.remove(cg);
            
            if ( cg.isUnified() ) {

				GLogger.global.fine("ComplexTransformCall: call doesn't have next but it matched");
                
                CallVertex untraversedInputState = call2input.get(cg);
                
                Iterator<CallVertex> outIt = cg.outputs.iterator();
                while( outIt.hasNext() ) {
                    
                	CallVertex output = outIt.next();
                    
                    Form embeddedResultSituation = 
                        untraversedInputState.getForm().duplicateAndEmbed(cg.macroEdge.traversal,
                        		output.getForm()); // TODO !!??
                    
                    CallVertex nextState = getGraph().addVertex(new CallVertex(history.nextVertexId(),
                    		cg.macroEdge.getDest(), embeddedResultSituation));

					// FYI at this point we could like output to next_state
                    
                    getGraph().addEdge(new CallEdge(history.nextEdgeId(), untraversedInputState, nextState, macroEdge));
                    
        	        createNextCalls(history, lexicon, nextState);
                }
            }
            else {
                // TODO Is there anything to do in this case ??
				GLogger.global.fine("ComplexTransformCall: call is not active and it didn't match");
			}
            
        }

        GLogger.global.exiting("ComplexTransformCall", "next");
    }



    public ComplexTransform getTransformSystemTo(Name guardName, Form guard, CallVertex s)
    {
    	/*
        Set arc_set = new HashSet();
        Set exit_set = new HashSet();
        ComplexTransformNode sn = s.procedureNodeNetworkTo(arc_set, exit_set);

        Vector arc_vector = new Vector();
        Iterator arc_it = arc_set.iterator();
        while( arc_it.hasNext() ) {
            ComplexSystmArc arc = (ComplexSystmArc) arc_it.next();
            arc_vector.add(arc);
        }

                // is this necessary??? !!!
        Set exits = new HashSet();
        Iterator exit_it = exit_set.iterator();
        while( exit_it.hasNext() ) {
            ComplexTransformNode exit = (ComplexTransformNode) exit_it.next();
            exits.add(exit);
        }

        return new ComplexTransform(guardName, sn, arc_vector, exits);
        */
    	return null;
    }

}
