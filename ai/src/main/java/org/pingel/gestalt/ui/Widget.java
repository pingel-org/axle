package org.pingel.gestalt.ui;

import java.awt.Point;
import java.awt.event.MouseEvent;

import org.pingel.gestalt.core.History;
import org.pingel.gestalt.core.Lexicon;

public interface Widget {
    
    public Widget mousePressed(MouseEvent e, History history, Lexicon lookupLexicon, Lexicon newLexicon);

    public boolean mouseClicked(MouseEvent e, History history, Lexicon lexicon);
    
    public void release(Point p, History history, Lexicon lookupLexicon, Lexicon newlexicon);
    
    public void drag(Point p, History history, Lexicon lexicon);
    
    public Point getCenter();
    
    public void setHighlighted(boolean h);
 
    public Point getBounds();

}
