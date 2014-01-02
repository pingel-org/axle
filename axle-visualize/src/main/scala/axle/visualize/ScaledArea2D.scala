package axle.visualize

import java.awt.{ Dimension, BasicStroke, Color, Paint, Stroke, Insets, Graphics, Graphics2D, Point }
import javax.swing.JPanel
import java.awt.event.MouseEvent
import java.awt.FontMetrics
import math.{ min, abs }
import axle.algebra._ //Portionable
import axle.quanta._
import Angle._

// http://www.apl.jhu.edu/~hall/java/Java2D-Tutorial.html

case class Point2D[X, Y](x: X, y: Y)

case class ScaledArea2D[X: Portionable, Y: Portionable](
  width: Int, height: Int, pad: Int,
  minX: X, maxX: X, minY: Y, maxY: Y) {

  val nonZeroArea = (!(minX equals maxX)) && (!(minY equals maxY))

  val xPortionable = implicitly[Portionable[X]]
  val yPortionable = implicitly[Portionable[Y]]

  val drawableWidth = width - (2 * pad)
  val drawableHeight = height - (2 * pad)

  def framePoint(sp: Point2D[X, Y]): Point = new Point(
    pad + (drawableWidth * xPortionable.portion(minX, sp.x, maxX)).toInt,
    height - pad - (drawableHeight * yPortionable.portion(minY, sp.y, maxY)).toInt
  )

  def fillOval(g2d: Graphics2D, p: Point2D[X, Y], width: Int, height: Int): Unit = {
    if (nonZeroArea) {
      val fp = framePoint(p)
      g2d.fillOval(fp.x - width / 2, fp.y - height / 2, width, height)
    }
  }

  def drawOval(g2d: Graphics2D, p: Point2D[X, Y], width: Int, height: Int): Unit = {
    if (nonZeroArea) {
      val fp = framePoint(p)
      g2d.drawOval(fp.x - width / 2, fp.y - height / 2, width, height)
    }
  }

  def drawLine(g2d: Graphics2D, p0: Point2D[X, Y], p1: Point2D[X, Y]): Unit = {
    if (nonZeroArea) {
      val fp0 = framePoint(p0)
      val fp1 = framePoint(p1)
      g2d.drawLine(fp0.x, fp0.y, fp1.x, fp1.y)
    }
  }

  def fillRectangle(g2d: Graphics2D, p0: Point2D[X, Y], p1: Point2D[X, Y]): Unit = {
    if (nonZeroArea) {
      val fp0 = framePoint(p0)
      val fp1 = framePoint(p1)
      g2d.fillRect(min(fp0.x, fp1.x), min(fp0.y, fp1.y), abs(fp0.x - fp1.x), abs(fp0.y - fp1.y))
    }
  }

  def drawRectangle(g2d: Graphics2D, p0: Point2D[X, Y], p1: Point2D[X, Y]): Unit = {
    if (nonZeroArea) {
      val fp0 = framePoint(p0)
      val fp1 = framePoint(p1)
      g2d.drawRect(min(fp0.x, fp1.x), min(fp0.y, fp1.y), abs(fp0.x - fp1.x), abs(fp0.y - fp1.y))
    }
  }

  def drawString(g2d: Graphics2D, s: String, p: Point2D[X, Y]): Unit = {
    if (nonZeroArea) {
      val fp = framePoint(p)
      g2d.drawString(s, fp.x, fp.y)
    }
  }

  def drawStringAtAngle(g2d: Graphics2D, fontMetrics: FontMetrics, s: String, p: Point2D[X, Y], angle: Angle.Q): Unit = {
    if (nonZeroArea) {
      val fp = framePoint(p)
      val a = (angle in rad).magnitude.doubleValue
      g2d.translate(fp.x, fp.y + fontMetrics.getHeight)
      g2d.rotate(a)
      g2d.drawString(s, 0, 0)
      g2d.rotate(-1 * a)
      g2d.translate(-fp.x, -fp.y - fontMetrics.getHeight)
    }
  }

}
