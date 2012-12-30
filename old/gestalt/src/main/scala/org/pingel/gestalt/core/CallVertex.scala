package org.pingel.gestalt.core

import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Point
import java.awt.geom.Ellipse2D
import axle.graph._

case class CallVertex(id: Int, tv: TransformVertex, var form: Form)
  extends Vertex[CallEdge] {

  val center = new Point()
  val radius = 15

  def getId() = id

  def getTransformVertex() = tv

  def clearForm(): Unit = {
    form = null
  }

  def setForm(f: Form): Unit = {
    form = f
  }

  def getCenter() = center

  def move(p: Point): Unit = {
    // println("CallVertex.move: p = " + p)
    center.move(p.x, p.y)
    if (form != null) {
      form.arrange(p)
    }
  }

  def getForm() = form

  def printTo(sb: StringBuffer): Unit = {
    if (tv.isExit) {
      sb.append("*")
    } else {
      sb.append(" ")
    }
    sb.append("<" + tv.name + ", " + getId() + ", ")
    form.printToStream(new Name("s" + id), sb)
    sb.append(">")
  }

  // See http://www.pitt.edu/~nisg/cis/web/cgi/rgb.html for colors
  val darkOrange = new Color(255, 140, 0) // Dark orange 
  // 178, 34, 34); // firebrick
  val turquoise = new Color(64, 224, 208) // turquose

  def paint(g: Graphics): Unit = {
    val g2d = g.asInstanceOf[Graphics2D]

    if (form == null) {
      val inCircle = new Ellipse2D.Double(center.x - radius, center.y - radius, 2 * radius, 2 * radius)
      g2d.setColor(darkOrange)
      g2d.fill(inCircle)
      g2d.setColor(Color.BLACK)
      g2d.draw(inCircle)
    } else {
      // println("painting cv's form: " + getForm().toString())
      form.paint(g)
    }

  }
}
