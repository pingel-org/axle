package org.pingel.gestalt.core

import java.awt.Point
import java.awt.event.MouseEvent
import org.pingel.gestalt.ui.Widget

case class FormFactory(archetype: Form) extends Widget {
  
	// removed during scalification:
	// public FormFactory(){}

    def createForm(): Form = {
        // This is called by mousePressed
        var f = archetype.duplicate()
        f.arrange(getArchetype().getCenter())
        f.setDetachable(true)
        f
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

    def getArchetype() = archetype

    def toString() = archetype.getClass().getName() + " factory"

    def mousePressed(e: MouseEvent, history: History, lookupLexicon: Lexicon, newLexicon: Lexicon): Widget = {
        println("FormFactoryController.mousePressed")
        val p = e.getPoint()
        if( getArchetype().contains(p)) {
            val f = createForm()
            newLexicon.put(new Name(), f)
            return f
        }
        null
    }

    def mouseClicked(e: MouseEvent, history: History, lexicon: Lexicon) = false

    def release(p: Point, history: History, lookupLexicon: Lexicon, newlexicon: Lexicon): Unit = { }
    
    def drag(p: Point, history: History, lexicon: Lexicon): Unit = {}

    def getCenter() = getArchetype().getCenter()

    def setHighlighted(h: Boolean): Unit = { }

    def getBounds() = getArchetype().getBounds()

}
