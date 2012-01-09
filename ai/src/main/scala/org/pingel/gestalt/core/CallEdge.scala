package org.pingel.gestalt.core;

import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Point
import java.awt.Polygon
import java.awt.event.MouseEvent

import org.pingel.gestalt.ui.Widget
import org.pingel.util.DirectedGraphEdge

case class CallEdge(id: Int, v1: CallVertex, v2: CallVertex, transformEdge: TransformEdge)
extends DirectedGraphEdge[CallVertex]
with Widget
{

    val radius = 15
    var poly: Polygon = null

    updatePoly()

    def getSource() = v1
    def getDest() = v2

    def getId() = id

    def arrange(p: Point): Unit = {
    	getSource().getForm().arrange(p)
        val p2 = new Point(p)
        p2.translate(100, 0)
        getDest().getForm().arrange(p2)
        updatePoly()
    }
    
    // TODO this is not quite right
    def getBounds() = poly.getBounds().getLocation()

    def contains(p: Point) = poly.contains(p)

    def updatePoly(): Unit = {
        // println("CallEdge.updatePoly: center = " + getSource().getForm().center + ", radius = ")
        poly = new Polygon()
        poly.addPoint(getSource().getForm().center.x, getSource().getForm().center.y - radius)
        poly.addPoint(getSource().getForm().center.x, getSource().getForm().center.y + radius)
        poly.addPoint(getDest().getForm().center.x, getDest().getForm().center.y)
    }

    val navajoWhite = new Color(255, 222, 173) // Navajo White

    def paint(g: Graphics): Unit = {
        val g2d = g.asInstanceOf[Graphics2D]
        g2d.setColor(navajoWhite)
        g2d.fill(poly)
        g2d.setColor(Color.BLACK)
        g2d.draw(poly)
    }

    def drag(p: Point, history: History, lexicon: Lexicon): Unit = {

        println("CallEdge.mouseDragged")

        val origin = getCenter()
        val dp = new Point(p.x - origin.x, p.y - origin.y)

        getSource().move(p)
        
        val newDestPosition = new Point(getDest().getCenter())
        newDestPosition.translate(dp.x, dp.y)
        getDest().move(newDestPosition)
        
        updatePoly()
    }

    def mousePressed(e: MouseEvent, history: History, lookupLexicon: Lexicon, newLexicon: Lexicon): Widget = {
        println("CallEdge.mousePressed")
        val p = e.getPoint()
        if( contains(p) ) {
            return this
        }
        // TODO carry just a vertex
        // TODO see if a edge contains p
        // TODO see if the start vertex contains p
        null
    }

    def mouseClicked(e: MouseEvent, history: History, lexicon: Lexicon) = false

    def release(p: Point, history: History, lookupLexicon: Lexicon, newLexicon: Lexicon): Unit = { }

    def getCenter() = getSource().getCenter()

    def setHighlighted(h: Boolean): Unit = {}

}
