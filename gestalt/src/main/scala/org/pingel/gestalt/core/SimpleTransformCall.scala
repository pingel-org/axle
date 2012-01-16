package org.pingel.gestalt.core

case class SimpleTransformCall(
    override val id: Int,
    override val history: History,
    override val lexicon: Lexicon,
    override val transform: SimpleTransform,
    macro: TransformEdge)
extends CallGraph(id, history, lexicon, transform, macro)
{
	// TODO assert: out.variables equals bindings.keySet

    def replacements(): Map[Name, Form] = {
		GLogger.global.entering("SimpleTransformCall", "replacements")
		var replacements = Map[Name, Form]()
		var map_it = transform.map.keySet.iterator
		while( map_it.hasNext ) {
		    val mi = map_it.next()
		    val variable = transform.map(mi)
		    val replacement = unifier.get(mi)

		    // too strict !!
		    val already = replacements(variable)
		    if( already != null ) {
		    	GLogger.global.info("SimpleSystmCall.replacements variable is already bound to " + already.toString())
		    	// TODO Shouldn't this have already been done in the Call superclass ??
		    	// matched = false;
		    	_hasNext = false
		    	return null
		    }

		    replacements += variable -> replacement
		}

        replacements
    }

    def next(history: History, lexicon: Lexicon): Unit = {
		GLogger.global.entering("SimpleSystmCall", "next")
        val repls = replacements()
        if( repls == null ) {
            return
        }
		val resultForm = lexicon.getForm(transform.outName).duplicateAndReplace(repls)
		val resultVertex = addVertex(new CallVertex(history.nextVertexId(), transform.exitNode, resultForm))
		GLogger.global.info("Transform result is " + resultForm.toString())
		addEdge(new CallEdge(history.nextEdgeId(), start, resultVertex, macroEdge))
		outputs += resultVertex
		_hasNext = false
        GLogger.global.exiting("SimpleTransformCall", "next")
    }

    def cost() = transform.cost

}
