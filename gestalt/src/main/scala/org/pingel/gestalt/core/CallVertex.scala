package org.pingel.gestalt.core

import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Point
import java.awt.geom.Ellipse2D

import org.pingel.axle.graph.DirectedGraphVertex
import org.pingel.axle.util.Printable

case class CallVertex(id: Int, tv: TransformVertex, var form: Form)
  extends DirectedGraphVertex[CallEdge] {

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
    //        System.out.println("CallVertex.move: p = " + p);
    center.move(p.x, p.y)
    if (form != null) {
      form.arrange(p)
    }
  }

  def getForm() = form

  def printTo(out: Printable): Unit = {
    if (tv.isExit) {
      out.print("*")
    } else {
      out.print(" ")
    }
    out.print("<" + tv.name + ", " + getId() + ", ")
    form.printToStream(new Name("s" + id), out)
    out.print(">")
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
      //            System.out.println("painting cv's form: " + getForm().toString());
      form.paint(g)
    }

  }
}
