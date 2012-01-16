package org.pingel.gestalt.core

import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Point
import java.awt.geom.Ellipse2D

import org.pingel.axle.graph.LabelledDirectedGraph

abstract case class Transform(guardName: Name)
  extends Logos
  with LabelledDirectedGraph[TransformVertex, TransformEdge] {
  var start: TransformVertex = null
  var exits = Set[TransformVertex]()

  override def addVertex(tv: TransformVertex) = {
    super.addVertex(tv)

    if (tv.isStart) {
      start = tv
    }
    if (tv.isExit) {
      exits += tv
    }
    tv
  }

  var center = new Point()

  def constructCall(id: Int, history: History, lexicon: Lexicon, macro: TransformEdge): CallGraph

  def arrange(p: Point): Unit = {
    center.move(p.x, p.y)
  }

  def getCenter() = center

  def paint(g: Graphics): Unit = {
    val g2d = g.asInstanceOf[Graphics2D]
    g2d.setColor(Color.RED)
    val circle = new Ellipse2D.Double(center.x - radius, center.y - radius, 2 * radius, 2 * radius)
    g2d.fill(circle)
    g2d.setColor(Color.BLACK)
    g2d.draw(circle)
  }

  def move(p: Point): Unit = {
    center.move(p.x, p.y)
  }

  def contains(p: Point) = distanceSquared(center, p) < radius * radius

}
