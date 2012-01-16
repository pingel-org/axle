package org.pingel.gestalt.core

import java.awt.Point
import java.awt.event.MouseEvent
import org.pingel.gestalt.ui.Widget

case class TransformFactory(transform: Transform, factoryLexicon: Lexicon)
  extends Widget {

  def getTransform() = transform

  def mousePressed(e: MouseEvent, history: History, lookupLexicon: Lexicon, newLexicon: Lexicon): Widget = {
    println("TransformFactory.mousePressed")
    val p = e.getPoint()
    val transform = getTransform()
    if (transform.contains(p)) {
      val cg = transform.constructCall(history.nextCallId(), history, factoryLexicon, null)
      cg.move(transform.getCenter())
      cg.arrange(transform.getCenter())
      return cg
    }
    return null
  }

  def mouseClicked(e: MouseEvent, history: History, lexicon: Lexicon) = false

  def release(p: Point, history: History, lookupLexicon: Lexicon, newLexicon: Lexicon): Unit = {}

  def drag(p: Point, history: History, lexicon: Lexicon): Unit = {}

  def getCenter() = getTransform().getCenter()

  def setHighlighted(h: Boolean): Unit = {}

  // TODO: not quite right
  def getBounds() = getTransform().getCenter()

}
