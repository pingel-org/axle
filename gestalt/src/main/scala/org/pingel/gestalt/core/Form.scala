package org.pingel.gestalt.core

import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Point
import java.awt.event.MouseEvent
import java.awt.geom.Ellipse2D
import org.pingel.gestalt.ui.Widget
import org.pingel.ptype.PType
import org.pingel.axle.util.Printable

abstract case class Form(lambda: Lambda=new Lambda())
extends Logos
with Widget
with Comparable[Form]
{
	var transform2type = Map[Transform, PType]()

	def addTransform(transform: Transform, pType: PType): Unit = {
		transform2type += transform -> pType
	}

	def getType(transform: Transform) = transform2type(transform)

	def getLambda() = lambda
	val center = new Point()
	var color = Color.WHITE

	var parent: ComplexForm = null

//    private CallVertex outputFrom = null;

	var inputTo: CallGraph = null

    var detachable = false

    def size(): Int
    
    def equals(other: Form): Boolean

    def unify(freeLambda: Lambda, target: Form, unifier: Unifier): Boolean
    
    def duplicate(): Form

    def duplicateAndReplace(replacements: Map[Name, Form]): Form

    def traverse(traversal: Traversal): Form = {
		if( traversal == null ) {
		    return this
		}
		else {
		    return _traverse(traversal, 0)
		}
    }

    def _traverse(traversal: Traversal, i: Int): Form

    def _duplicateAndEmbed(traversal: Traversal, i: Int, s: Form): Form

    def duplicateAndEmbed(traversal: Traversal, s: Form): Form = {
		if( traversal == null ) {
		    return s
		}
		else {
		    return _duplicateAndEmbed(traversal, 0, s)
		}
    }

//  public String toString()
//  {
//      Printable psb = new PrintableStringBuffer(new StringBuffer());
//      this.printToStream(null, psb);
//      return psb.toString();
//  }

    def printToStream(name: Name, out: Printable): Unit = {
    		if( name != null ) {
    			out.print("form " + name)
    			if( lambda != null ) {
    				out.print(" [")
    				val it = lambda.getNames().iterator
    				while( it.hasNext ) {
    					out.print(it.next().toString())
    					if( it.hasNext ) {
    						out.print(" ")
    					}
    				}
    				out.print("]")
    			}
    			out.print(" ")
    		}
    }
    
    def setParent(parent: ComplexForm): Unit = {
        this.parent = parent
    }

    def getParent() = parent

    def getCenter() = center

    def clearInputTo(): Unit = {
      inputTo = null
    }

    def getInputTo() = inputTo

    def reAttachToCallGraph(history: History, lexicon: Lexicon): Unit = {
        println("Form.reAttachToCallGraph: begin")
        if( parent == null ) {
            println("Form.reAttachToCallGraph: parent == null")
            if( inputTo != null ) {
                println("Form.reAttachToCallGraph: inputTo != null")
                inputTo.detachInput(history, lexicon)
                val cv = new CallVertex(history.nextVertexId(), inputTo.transform.start, this)
                var cg = inputTo.transform.constructCall(history.nextCallId(), history, lexicon, null)
                cg.unify(history, cv)
                if( cg.isUnified() ) {
                    while( cg.hasNext() ) {
                        cg.next(history, lexicon)
                    }
                    inputTo = cg
                }
                else {
                    cg.detachInput(history, lexicon)
                    history.remove(cg)
                    inputTo = null
                }
            }
        }
        else {
            parent.reAttachToCallGraph(history, lexicon)
        }
    }
    
    def contains(p: Point): Boolean = {
        println("Form.contains: p = " + p + ", center = " + center + ", radius = " + radius)
        distanceSquared(center, p) <= radius*radius
    }


    def moveOK(p: Point): Boolean = {
        if( parent != null ) {
            if( p.y <= parent.center.y ) {
                return false
            }
            if( this == parent.getLeft() ) {
                if( p.x >= parent.center.x) {
                    return false
                }
            }
            else {
                if( p.x <= parent.center.x) {
                    return false
                }
            }
        }
        return true
    }

    def getBounds(): Point

    def arrange(p: Point): Unit

    def move(p: Point): Unit = {
        center.move(p.x, p.y)
        // TODO this must work differently !!!
        if( inputTo != null ) {
            for( edge <- inputTo.getGraph().getEdges() ) {
                edge.updatePoly()
            }
        }
    }

    def memberIntersects(other: Form): Form
    
    def memberContains(p: Point): Form
    
    def intersects(other: Form): Boolean = 
    	distanceSquared(center, other.center) < 4*radius*radius

    def intersects(cv: CallVertex): Boolean = {
        if( cv.getForm() == null ) {
            System.err.println("Form.intersects: cv.getForm == null")
            return false
        }
        
        distanceSquared(center, cv.getForm().getCenter()) < 4*radius*radius
    }
    
    def setDetachable(b: Boolean): Unit = {
        detachable = b
    }
    
    def isDetachable() = detachable

    // Detach 'this' from parent, and replace with new Blank
    def detachFromParent(history: History, lexicon: Lexicon): Unit = {
        if( parent == null ) {
            System.err.println("attempting to detach something that has no parent")
            System.exit(1)
        }

        val blank = new Blank()
        blank.arrange(center)
        
        if( this == parent.getLeft() ){
            parent.setLeft(blank)
        }
        else {
            parent.setRight(blank)
        }
        blank.setParent(parent)
        
        parent.reAttachToCallGraph(history, lexicon)
        
        parent = null
        lexicon.put(new Name(), this)
    }

    def paint(g: Graphics): Unit = {
        val g2d = g.asInstanceOf[Graphics2D]

        if( highlighted ) {
            g2d.setColor(Color.YELLOW)
        }
        else {
            g2d.setColor(color)
        }
        
        val circle = new Ellipse2D.Double(center.x - radius, center.y - radius, 2*radius, 2*radius)
        g2d.fill(circle)
        g2d.setColor(Color.BLACK)
        g2d.draw(circle)
    }

    def mouseClicked(e: MouseEvent, history: History, lexicon: Lexicon): Boolean = {
        println("FormController.mouseClicked")
        // Delete the selected form
        if( contains(e.getPoint()) && isDetachable() ) {
            if( e.getButton() == 2 ) {
                if( getParent() != null ) {
                    detachFromParent(history, lexicon)
                }
                lexicon.remove(this)
            }
            return true
        }
        false
    }

    def drag(p: Point, history: History, lexicon: Lexicon): Unit = {
    	println("FormController.mouseDragged")
        if( moveOK(p) ) {
            move(p)
        }
    }

    def mousePressed(e: MouseEvent, history: History, lookupLexicon: Lexicon, newLexicon: Lexicon): Widget = {
        println("FormController.mousePressed")
        val p = e.getPoint()
        val f = memberContains(p)
        if( f != null ) {
        	if( e.getButton() == 3 ) {
                if( f.isDetachable() ) {
                    val parent = f.getParent()
                    if( parent != null ) {
                        f.detachFromParent(history, newLexicon)
                    }
                    else if ( f.getInputTo() != null ) {
                        // TODO smells bad
                        val cg = f.getInputTo()
                        cg.detachInput(history, newLexicon)
                    }
                }
            }
            return f
        }
        null
    }

    def release(p: Point, history: History, lookupLexicon: Lexicon, newlexicon: Lexicon): Unit = {
      
    	println("FormController.mouseReleased")

        if( ! moveOK(p) ) {
            return
        }

        if( parent != null ) {
            return
        }
 
        for( other <- newlexicon.getTopForms() ) {
            if( other != this ) {
                other.memberIntersects(this) match {
                  case blank: Blank => {
                    if ( intersects(blank) ) {
                        val binaryParent = blank.getParent()
                        if( blank == binaryParent.getLeft() ){
                            binaryParent.setLeft(this)
                        }
                        else {
                            binaryParent.setRight(this)
                        }
                        setParent(blank.getParent())
                        blank.setParent(null)
                        reAttachToCallGraph(history, newlexicon)
                        return
                    }
                  }
                  case _ => {}
                }
            }
        }
        
        for( cg <- history.getCalls() ) {
            
            if( parent == null && cg.intersects(this) && ! cg.isUnified()) {

                val cv = new CallVertex(history.nextVertexId(), cg.transform.start, this)
                println("calling unify on callGraphToAttachTo")
                cg.unify(history, cv)
                if( cg.isUnified() ) {
                    cg.proceed(history, lookupLexicon)
                    cg.arrange(center)
                }
            }
        }
    }

}
