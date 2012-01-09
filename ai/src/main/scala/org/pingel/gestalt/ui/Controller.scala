package org.pingel.gestalt.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import org.pingel.gestalt.core.History;
import org.pingel.gestalt.core.Lexicon;

public class Controller
extends JPanel
implements MouseListener, MouseMotionListener, ActionListener
{
    static final long serialVersionUID = 1;
    
    Renderer renderer;
    History history;
    Lexicon lookupLexicon;
    Lexicon newLexicon;
        
    Widget carried = null;
    
    public Controller(History history, Lexicon lookupLexicon, Lexicon newLexicon)
    {
        super(new BorderLayout());

        lookupLexicon.addFactories();

        this.renderer = new Renderer(this, history, lookupLexicon, newLexicon);
        this.history = history;
        this.lookupLexicon = lookupLexicon;
        this.newLexicon = newLexicon;
        
        
    }

    private Point grabOffset = new Point(0, 0);
    
    public void setGrabOffsetTo(Point p, Point origin)
    {
        grabOffset.move(origin.x - p.x, origin.y - p.y);
    }
    
    public void translateByGrabOffset(Point p)
    {
        p.translate(grabOffset.x, grabOffset.y);
    }

    public void mouseDragged(MouseEvent e)
    {
        System.out.println("Controller.mouseDragged");
        
        if( carried != null ) {
            Point p = new Point(e.getPoint());
            translateByGrabOffset(p);
            carried.drag(p, history, newLexicon);
            repaint();
        }

    }

    public void mouseReleased(MouseEvent e) {
        
        System.out.println("Controller.mouseReleased");

        // TODO make sure the mouse is released over the drawing panel

        if( carried != null ) {
            Point p = new Point(e.getPoint());
            translateByGrabOffset(p);
            carried.release(p, history, lookupLexicon, newLexicon);
            Point bounds = carried.getBounds();
            renderer.checkBounds(new Dimension(bounds.x, bounds.y));
            carried = null;
            repaint();
        }
        
    }
        
    public void mouseMoved(MouseEvent e) {}

    
    private void carry(MouseEvent e, Widget toCarry)
    {
        renderer.unHighlight();
        renderer.setHighlighted(toCarry);
        Point grabbedCenter = toCarry.getCenter();
        setGrabOffsetTo(e.getPoint(), grabbedCenter);
        carried = toCarry;
        repaint();
    }
    
    public void mousePressed(MouseEvent e) {

        System.out.println("Controller.mousePressed");

        for( Widget ff : lookupLexicon.getFormFactories() ) {
            Widget toCarry = ff.mousePressed(e, history, lookupLexicon, newLexicon);
            if( toCarry != null ) {
                carry(e, toCarry);
                return;
            }
        }

        for( Widget tf : lookupLexicon.getTransformFactories() ) {
            Widget toCarry = tf.mousePressed(e, history, lookupLexicon, newLexicon);
            if( toCarry != null ) {
                carry(e, toCarry);
                return;
            }
        }

        for( Widget form : newLexicon.getTopForms() ) {
            Widget toCarry = form.mousePressed(e, history, lookupLexicon, newLexicon);
            if( toCarry != null ) {
                carry(e, toCarry);
                return;
            }
        }

        for( Widget cg : history.getCalls() ) {
            Widget toCarry = cg.mousePressed(e, history, lookupLexicon, newLexicon);
            if( toCarry != null ) {
                carry(e, toCarry);
                return;
            }
        }
        

        if( renderer.unHighlight() ) {
            repaint();
        }
    }
    
    
    public void mouseEntered(MouseEvent e) {}
    
    public void mouseExited(MouseEvent e) {}
    
    public void mouseClicked(MouseEvent e) {

        System.out.println("Controller.mouseClicked");

        
        for( Widget f : newLexicon.getTopForms() ) {
            if( f.mouseClicked(e, history, newLexicon) ) {
                repaint();
                return;
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
    
    public void actionPerformed(ActionEvent event) {}

}
