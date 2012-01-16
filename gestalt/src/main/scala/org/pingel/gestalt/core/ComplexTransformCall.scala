package org.pingel.gestalt.core

case class ComplexTransformCall(
    override val id: Int,
    override val history: History,
    override val lexicon: Lexicon, 
    override val transform: ComplexTransform, macro: TransformEdge)
extends CallGraph(id, history, lexicon, transform, macro)
{
    var activeCalls = Set[CallGraph]()
    var call2input = Map[CallGraph, CallVertex]()
    var initialized = false

    def cheapestCall(): CallGraph = {
        var cheapest: CallGraph = null
        var cheapest_cost = 999999.0
		GLogger.global.info("ComplexTransformCall.cheapestCall: activeCalls.size() = " + activeCalls.size)
		for( call <- activeCalls ) {
            GLogger.global.info("cheapestCall in loop")
            var current_cost = call.cost()
            if( call == null || current_cost < cheapest_cost ) {
                cheapest_cost = current_cost
                cheapest = call
            }
        }
        GLogger.global.info("cheapestCall done")
        cheapest
    }

    def networkCost(cv: CallVertex): Double = {
        var running_total = 0.0
        for( outputEdge <- outputEdgesOf(cv) ) {
            running_total += networkCost(outputEdge.getDest())
        }
        1 + running_total
    }

    def cost() = (start == null) match {
      case true => 0.0
      case false => networkCost(start)
    }

    def createNextCalls(history: History, lexicon: Lexicon, state: CallVertex): Unit = {
      
        GLogger.global.entering("ComplexTransformCall", "createNextCalls: state = " + state.toString())
        GLogger.global.fine("state.pnode.isExit() = " + state.getTransformVertex().isExit )
        GLogger.global.fine("prior to state.pnode.createNextCalls, there are " +
                activeCalls.size + " calls in active_calls")

        if( state.getTransformVertex().isExit ) {
            outputs += state
        }

        val outputEdges = transform.getGraph().outputEdgesOf(state.getTransformVertex())
        
        for( nextEdge <- outputEdges ) {
            
            GLogger.global.info("found node successor with system " + nextEdge.getDest().name )
            
            val nextTransform = lexicon.getTransform(nextEdge.transformName)
            
            val traversedState = new CallVertex(history.nextVertexId(),
            		nextTransform.start,
                    state.getForm().traverse(nextEdge.traversal))
            
            val nextCall = nextTransform.constructCall(history.nextCallId(), history, lexicon, nextEdge)
                
            nextCall.unify(history, traversedState)
            
            call2input += nextCall -> state
            
            activeCalls += nextCall

            GLogger.global.fine("after state.pnode.createNextCalls, there are " + activeCalls.size + " calls in active_calls")
        }
    }
    
    def next(history: History, lexicon: Lexicon): Unit = {
		GLogger.global.entering("ComplexTransformCall", "next")

		if( ! initialized ) {
			createNextCalls(history, lexicon, start)
			initialized = true
		}
    
        val cg = cheapestCall()
        if( cg == null ) {
            _hasNext = false
		    GLogger.global.fine("ComplexTransformCall.next: call == null. end")
            return
        }

		GLogger.global.info("ComplexTransformCall's call is " + cg.toString())
        
        if( cg.hasNext() ) {
            cg.next(history, lexicon)
        }
        else {
          
            activeCalls -= cg
            
            if ( cg.isUnified() ) {

				GLogger.global.fine("ComplexTransformCall: call doesn't have next but it matched")
                
                val untraversedInputState = call2input(cg)

                for( output <- cg.outputs ) {
                    val embeddedResultSituation = 
                        untraversedInputState.getForm().duplicateAndEmbed(cg.macroEdge.traversal,
                        		output.getForm()) // TODO !!??
                    
                    val nextState = addVertex(new CallVertex(history.nextVertexId(), cg.macroEdge.getDest(), embeddedResultSituation))

					// FYI at this point we could like output to next_state
                    
                    addEdge(new CallEdge(history.nextEdgeId(), untraversedInputState, nextState, macroEdge))
        	        createNextCalls(history, lexicon, nextState)
                }
            }
            else {
                // TODO Is there anything to do in this case ??
				GLogger.global.fine("ComplexTransformCall: call is not active and it didn't match");
			}
            
        }

        GLogger.global.exiting("ComplexTransformCall", "next")
    }

    def getTransformSystemTo(guardName: Name, guard: Form, s: CallVertex): ComplexTransform = {
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
    	null
    }

}
