package org.pingel.gestalt.ui

import java.awt.Point
import java.awt.event.MouseEvent

import org.pingel.gestalt.core.History
import org.pingel.gestalt.core.Lexicon

trait Widget {
    
	def mousePressed(e: MouseEvent, history: History, lookupLexicon: Lexicon, newLexicon: Lexicon): Widget

	def mouseClicked(e: MouseEvent, history: History, lexicon: Lexicon): Boolean
    
	def release(p: Point, history: History, lookupLexicon: Lexicon, newlexicon: Lexicon): Unit
    
	def drag(p: Point, history: History, lexicon: Lexicon): Unit
    
	def getCenter(): Point
    
	def setHighlighted(h: Boolean): Unit
 
	def getBounds(): Point

}
