package org.pingel.gestalt.core

import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Point
import java.awt.event.MouseEvent
import java.awt.geom.Ellipse2D

import org.pingel.gestalt.ui.Widget
import org.pingel.axle.graph.DirectedGraph
import org.pingel.axle.util.Printable
import org.pingel.axle.util.PrintableStringBuffer

import scala.collection._

abstract case class CallGraph(id: Int, history: History, lexicon: Lexicon, transform: Transform, macroEdge: TransformEdge)
extends Widget
with DirectedGraph[CallVertex, CallEdge]
{

	var in: CallVertex = null
    var start: CallVertex = null

    val radius = 15
    var highlighted = false
    var center = new Point(0, 0)
    
    var unificationAttempted = false
    var unificationSucceeded = false
    var _hasNext = true
    var unifier = new Unifier()
    var outputs = new mutable.ListBuffer[CallVertex]()

    GLogger.global.entering("Call", "<init>")
    history.addCall(this)
    var guard = lexicon.getForm(transform.guardName).duplicate()
    guard.arrange(center)

    def unify(history: History, in: CallVertex): Unit = {
        this.in = in
        start = addVertex(new CallVertex(history.nextVertexId(), transform.start, in.getForm()))
        unificationSucceeded = guard.unify(guard.lambda, start.getForm(), unifier)
        if( ! unificationSucceeded ) {
            GLogger.global.fine("Call.constructor guard is not met")
            return
        }
        unificationAttempted = true
        _hasNext = true
    }

    def isUnified() = unificationSucceeded

	def getId() = this.id

	def getStart() = start

    def getOutputs() = outputs

	def cost(): Double
	
	def next(history: History, lexicon: Lexicon): Unit
	
	def hasNext() = _hasNext

    /* TODO deprecate this */
    
    def proceed(history: History, lexicon: Lexicon): Unit = {
        while( hasNext ) {
            next(history, lexicon)
        }
    }
    
    def printNetworkTo(cv: CallVertex, out: Printable, i: Int, callId: Int): Unit = {
        out.indent(i)
        cv.printTo(out)
        if( callId != -1 ) {
            out.print(" " + callId)
        }
        out.println()
        for( outEdge <- outputEdgesOf(cv) ) {
            printNetworkTo(outEdge.getDest(), out, i+1, cv.getId())
        }
    }
    
	def printNetworkTo(out: Printable, i: Int): Unit = {
		out.indent(i)
		if( start == null ) {
			out.println("error")
		}
		else {
		    printNetworkTo(start, out, i+1, -1)
		}
	}
	
	def toString(): String = {
		var psb = new PrintableStringBuffer(new StringBuffer())
		printNetworkTo(psb, 0)
		psb.toString()
	}

//    public void setHighlighted(boolean h)
//    {
//        highlighted = h;
//    }

//    public Form memberContains(Point p)
//    {
//        for( CallVertex cv : this.getGraph().getVertices() ) {
//            if( cv.getForm() != null ) {
//                Form f = cv.getForm().memberContains(p);
//                if( f != null ) {
//                    return f;
//                }
//            }
//        }
//        return null;
//    }
    
    def _detachInput(history: History, lexicon: Lexicon, cv: CallVertex): Unit = {
        val f = cv.getForm()
        f.clearInputTo()
        cv.clearForm()
        lexicon.remove(f)

        for( nextVertex <- getSuccessors(cv) ) {
            // TODO delete sub-graph
            _detachInput(history, lexicon, nextVertex)
        }

        val nextCG = f.getInputTo()
        if( nextCG != null ) {
            nextCG.detachInput(history, lexicon)
        }

    }
    
    def detachInput(history: History, lexicon: Lexicon): Unit = {
        unificationSucceeded = false
        unificationAttempted = false
        guard = null
        unifier = new Unifier()
        outputs = new mutable.ListBuffer[CallVertex]()
        removeAllEdgesAndVertices() // was: graph = new DG[]()
        _detachInput(history, lexicon, in)
    }

//    public Point getEntryCenter()
//    {
//        return center;
//    }

    
    def arrange(p: Point): Unit = {
      
        if( start == null ) {
            return
        }

        start.move(p)
        
        val p2 = new Point(p.x, p.y)
        p2.translate(100, 0)

        // TODO this should recurse through the nodes and arrange them
        // according to some sane layout

        // arrange all TransformVertexes as large circles (or perhaps dots), and then all CallVertices within them
        
        // Should the TransformVerctices already be layed out before the unification attempt?
        
        // Why is the unification attempt so off/on?  I think that's OK given that "unification"
        // happens exactly when a form is presented to a Transform.  But there are perhaps other
        // ways of thinking about this.

        for( tv <- this.transform.getGraph().getVertices() ) {
        		// arrange these in a circle
        		// then iterate through each of the CallVertexes that reference this TransformVertex
        		// arrange them in a circle
        }

        for( cv <- getVertices() ) {
            if( cv != start ) {
                cv.move(p2)
            }
            p2.translate(0, 50)
            // find the center of the corresponding TransformVertex and pick an angle at which to radiate
            // from the TransformVertex
        }

        for( te <- this.transform.getGraph().getEdges() ) {
        	// TODO: te.updatePoly()
        }

        for( ce <- getEdges() ) {
        	ce.updatePoly()
        }

    }
    
    def move(p: Point): Unit = {
        guard.move(p)
        val dp = new Point(p.x - center.x, p.y - center.y)
        center.move(p.x, p.y)
        for( cv <- getVertices() ) {
            var newPosition = new Point(cv.getForm().center)
            newPosition.translate(dp.x, dp.y)
            cv.move(newPosition)
        }
        for( ce <- getEdges() ) {
            ce.updatePoly()
        }
    }

	// TODO put this elsewhere
    def distanceSquared(p1: Point, p2: Point) =
    	(p2.x - p1.x)*(p2.x - p1.x) + (p2.y - p1.y)*(p2.y - p1.y)

    def intersects(f: Form) = distanceSquared(center, f.center) < 4*radius*radius

    def contains(p: Point): Boolean = {
    	guard.contains(p)
    	
//        if( ! unificationAttempted ) {
//        		System.out.println("CallGraph.contains: no unification attempted.  deferring to guard.contains");
//        		return guard.contains(p);
//        }
//        
//        for( CallVertex cv : getGraph().getVertices() ) {
//        		if( cv.getForm().contains(p) ) {
//        			System.out.println("CallGraph.contains: a vertex contains the point");
//            		return true;
//        		}
//        }
//
//        for( CallEdge ce : getGraph().getEdges() ) {
//        		if( ce.contains(p) ) {
//        			System.out.println("CallGraph.contains: an edge contains the point");
//        			return true;
//        		}
//        }
//        
//        return false;
    }
        
    def paint(g: Graphics) = {
        val g2d = g.asInstanceOf[Graphics2D]

        g2d.setColor(Color.ORANGE)
        val bigRadius = radius + 5
        val circle = new Ellipse2D.Double(center.x - bigRadius, center.y - bigRadius, 2*bigRadius, 2*bigRadius)
        g2d.fill(circle)
        g2d.setColor(Color.BLACK)
        g2d.draw(circle)

        if( ! unificationAttempted ) {
            guard.paint(g)
        }
        for( ce <- getEdges() ) {
//            System.out.println("CallGraph.paint calling CallEdge.paint");
            ce.paint(g)
        }
        for( cv <- getVertices() ) {
            cv.paint(g)
        }
    }

	def mouseClicked(e: MouseEvent, history: History, lexicon: Lexicon): Boolean = {
		println("CallGraph.mouseClicked")
        if( contains(e.getPoint())) {
            if( e.getButton() == 2 ) {
                detachInput(history, lexicon)
                history.remove(this)
                return true
            }
        }
        false
    }
    
    def drag(p: Point, history: History, lexicon: Lexicon): Unit = {
        println("CallGraph.mouseDragged")
        move(p)
    }

    def mousePressed(e: MouseEvent, history: History, lookupLexicon: Lexicon, newLexicon: Lexicon): Widget = {
//        for( CallEdge edge : graph.getEdges() ) {
//            Widget w = edge.mousePressed(e, history, lookupLexicon, newLexicon);
//            if( w != null ) {
//                return w;
//            }
//        }
        if( contains(e.getPoint())) {
            return this
        }
        null
    }
    
    def release(p: Point, history: History, lookupLexicon: Lexicon, newLexicon: Lexicon): Unit = {
        println("CallGraph.mouseReleased")
//        for( CallGraph cgOther : history.getCalls() ) {
//            for( CallVertex out : cg.getOutputs() ) {
//                if( out.getForm().intersects(cg.getStart()) ) {
//                    CallVertex start = cgOther.getStart();
//                    if( start == null ) {
//                        System.err.println("start == null!!!");
//                    }
//                    start.setForm(out.getForm());
//                    cgOther.proceed(history, newLexicon);
//                    // TODO shirt-circuit this loop
//                }
//            }
//        }
//        
//        Point bounds = cg.getBounds();
//        renderer.checkBounds(new Dimension(bounds.x, bounds.y));
//        
//        return true;
    }

    def getCenter() = center

    def setHighlighted(h: Boolean): Unit = {}

    def getBounds() = new Point(0,0) // TODO !!!
    
}
