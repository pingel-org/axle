package org.pingel.gestalt.core

import scala.collection._

import org.pingel.axle.util.Printable
import org.pingel.axle.util.PrintableStringBuffer

class History {

	var _nextEdgeId = 0
	var _nextVertexId = 0
	var _nextCallId = 0

	var calls = mutable.Set[CallGraph]()

    def nextEdgeId() = {
	  _nextEdgeId += 1
	  _nextEdgeId
    }

    def nextVertexId() = {
        _nextVertexId += 1
        _nextVertexId
    }

    def nextCallId() = {
      _nextCallId += 1
      _nextCallId
    }

	def addCall(call: CallGraph): Unit = {
        calls += call
    }

	def remove(call: CallGraph): Unit = {
        calls -= call
    }

	def getCalls(): Set[CallGraph] = calls

    def callVerticesByTransformVertex(tv: TransformVertex): Set[CallGraph] = null // TODO

    def printTo(out: Printable, lexicon: Lexicon): Unit = {
        for( call <- calls ) {
            out.println(call.getId() + " " + lexicon.getNameOf(call.transform))
            out.println()
            call.printNetworkTo(out, 0)
            out.println()
        }
    }

    def toString(lexicon: Lexicon): String = {
    	val psb = new PrintableStringBuffer(new StringBuffer())
        this.printTo(psb, lexicon)
        psb.toString()
    }

    def run(formName: String, transformName: String, lexicon: Lexicon): Unit = {
		val form = lexicon.getForm(new Name(formName))
        val transform = lexicon.getTransform(new Name(transformName))
        if (transform == null) {
            GLogger.global.severe("Couldn't find transform with name " + transformName)
            System.exit(1)
        }

        val call = transform.constructCall(nextCallId(), this, lexicon, null)
        val cv = new CallVertex(nextVertexId(), transform.start, form)
        call.unify(this, cv)

        while (call.hasNext()) {
            GLogger.global.info("top-level call\n")
            GLogger.global.info(call.toString() + "\n")
            call.next(this, lexicon)
        }

    }

}
