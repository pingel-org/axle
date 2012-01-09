package org.pingel.gestalt.core;

import java.awt.Point;
import java.awt.event.MouseEvent;

import org.pingel.gestalt.ui.Widget;


public class TransformFactory
implements Widget
{

    Transform transform;
    Lexicon factoryLexicon;
    
    public TransformFactory(Transform t, Lexicon factoryLexicon)
    {
        transform = t;
        this.factoryLexicon = factoryLexicon;
    }

    public Transform getTransform()
    {
        return transform;
    }

    public Widget mousePressed(MouseEvent e, History history, Lexicon lookupLexicon, Lexicon newLexicon)
    {

        System.out.println("TransformFactory.mousePressed");

        Point p = e.getPoint();
        
        Transform transform = getTransform();
        
        if( transform.contains(p) ) {

            CallGraph cg = transform.constructCall(history.nextCallId(), history, factoryLexicon, null);
            cg.move(transform.getCenter());
            cg.arrange(transform.getCenter());
            
            return cg;
        }
        
        return null;
    }

    public boolean mouseClicked(MouseEvent e, History history, Lexicon lexicon)
    {
        return false;
    }
    
    public void release(Point p, History history, Lexicon lookupLexicon, Lexicon newLexicon)
    {
    }
    
    public void drag(Point p, History history, Lexicon lexicon)
    {
    }

    public Point getCenter()
    {
        return getTransform().getCenter();
    }

    public void setHighlighted(boolean h)
    {
    }

    public Point getBounds()
    {
        return getTransform().getCenter(); // TODO not quite right
    }

}
