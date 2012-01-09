package org.pingel.gestalt.ui

import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Point
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener

import javax.swing.JPanel

import org.pingel.gestalt.core.History
import org.pingel.gestalt.core.Lexicon

case class Controller(history: History, lookupLexicon: Lexicon, newLexicon: Lexicon)
extends JPanel(new BorderLayout())
with MouseListener with MouseMotionListener with ActionListener
{
    val serialVersionUID = 1L

    var carried: Widget = null

    lookupLexicon.addFactories()

    val renderer = new Renderer(this, history, lookupLexicon, newLexicon)

    var grabOffset = new Point(0, 0)
    
    def setGrabOffsetTo(p: Point, origin: Point): Unit = grabOffset.move(origin.x - p.x, origin.y - p.y)

    def translateByGrabOffset(p: Point): Unit = p.translate(grabOffset.x, grabOffset.y)

    def mouseDragged(e: MouseEvent): Unit = {
        println("Controller.mouseDragged")
        if( carried != null ) {
        	val p = new Point(e.getPoint())
            translateByGrabOffset(p)
            carried.drag(p, history, newLexicon)
            repaint()
        }
    }

    def mouseReleased(e: MouseEvent): Unit = {
        println("Controller.mouseReleased")
        // TODO make sure the mouse is released over the drawing panel
        if( carried != null ) {
        	val p = new Point(e.getPoint())
            translateByGrabOffset(p)
            carried.release(p, history, lookupLexicon, newLexicon)
            val bounds = carried.getBounds()
            renderer.checkBounds(new Dimension(bounds.x, bounds.y))
            carried = null
            repaint()
        }
    }
        
    def mouseMoved(e: MouseEvent) {}

    def carry(e: MouseEvent, toCarry: Widget): Unit = {
        renderer.unHighlight()
        renderer.setHighlighted(toCarry)
        val grabbedCenter = toCarry.getCenter()
        setGrabOffsetTo(e.getPoint(), grabbedCenter)
        carried = toCarry
        repaint()
    }
    
    def mousePressed(e: MouseEvent): Unit = {

    	println("Controller.mousePressed")

        for( ff <- lookupLexicon.getFormFactories() ) {
        	val toCarry = ff.mousePressed(e, history, lookupLexicon, newLexicon)
            if( toCarry != null ) {
                carry(e, toCarry)
                return
            }
        }

        for( tf <- lookupLexicon.getTransformFactories() ) {
        	val toCarry = tf.mousePressed(e, history, lookupLexicon, newLexicon)
            if( toCarry != null ) {
                carry(e, toCarry)
                return
            }
        }

        for( form <- newLexicon.getTopForms() ) {
        	val toCarry = form.mousePressed(e, history, lookupLexicon, newLexicon)
            if( toCarry != null ) {
                carry(e, toCarry)
                return
            }
        }

        for( cg <- history.getCalls() ) {
        	val toCarry = cg.mousePressed(e, history, lookupLexicon, newLexicon)
            if( toCarry != null ) {
                carry(e, toCarry)
                return
            }
        }

        if( renderer.unHighlight() ) {
            repaint()
        }
    }
    
    
    def mouseEntered(e: MouseEvent): Unit = {}
    
    def mouseExited(e: MouseEvent): Unit = {}
    
    def mouseClicked(e: MouseEvent): Unit = {

    	println("Controller.mouseClicked")

        for( f <- newLexicon.getTopForms() ) {
            if( f.mouseClicked(e, history, newLexicon) ) {
                repaint()
                return
            }
        }

//        if( needsPainting ) {
//            repaint();
//        }
        
//        for( CallEdgeController cec : callEdgeControllers ) {
//            if( cec.mouseClicked(e) ) {
//                repaint();
//                return;
//            }
//        }

    }
    
    def actionPerformed(event: ActionEvent) {}

}
