
package org.pingel.gestalt.core

import java.awt.Graphics
import java.awt.Point
import org.pingel.axle.util.Printable

abstract class Logos {

  val radius = 15

  var highlighted = false

  def setHighlighted(h: Boolean): Unit = {
    highlighted = h
  }

  def contains(p: Point): Boolean

  def paint(g: Graphics): Unit

  def distanceSquared(p1: Point, p2: Point) = (p2.x - p1.x) * (p2.x - p1.x) + (p2.y - p1.y) * (p2.y - p1.y)

  def printToStream(name: Name, p: Printable): Unit

}
