package org.pingel.gestalt.core;

import java.awt.Point;
import java.awt.event.MouseEvent;

import org.pingel.gestalt.ui.Widget;


public class FormFactory
implements Widget
{
	protected Form archetype = null;

	public FormFactory()
	{
		
	}
	
    public FormFactory(Form archetype)
    {
        this.archetype = archetype;
    }


    public Form createForm()
    {
        // This is called by mousePressed
        
        Form f = archetype.duplicate();
        f.arrange(getArchetype().getCenter());
        f.setDetachable(true);

        return f;
    }

//    public Form createForm(Unifier unifier)
//    {
//        // This is called by org.pingel.causality.docalculus.*
//        
//        Form f = archetype.duplicateAndReplace();
//        f.arrange(getArchetype().getCenter());
//        f.setDetachable(true);
//    		
//        // TODO bind vars in unifier
//        return null;
//    }
    	
    public Form getArchetype()
    {
        return archetype;
    }

    public String toString()
    {
        return archetype.getClass().getName() + " factory";
    }


    public Widget mousePressed(MouseEvent e, History history, Lexicon lookupLexicon, Lexicon newLexicon) {

        System.out.println("FormFactoryController.mousePressed");
        
        Point p = e.getPoint();
        
        if( getArchetype().contains(p)) {
            Form f = createForm();
            newLexicon.put(new Name(), f);
            return f;
        }

        return null;
    }

    public boolean mouseClicked(MouseEvent e, History history, Lexicon lexicon)
    {
        return false;
    }
    
    public void release(Point p, History history, Lexicon lookupLexicon, Lexicon newlexicon)
    {
    }
    
    public void drag(Point p, History history, Lexicon lexicon)
    {
    }

    public Point getCenter()
    {
        return getArchetype().getCenter();
    }

    public void setHighlighted(boolean h)
    {
    }

    public Point getBounds()
    {
        return getArchetype().getBounds();
    }

}
