package axle.visualize

import java.awt.{ Dimension, BasicStroke, Color, Paint, Stroke, Insets, Graphics, Graphics2D, Point }
import javax.swing.JPanel
import java.awt.event.MouseEvent
import java.awt.FontMetrics
import math.{ min, abs }
import axle.quanta._
import Angle._

// http://www.apl.jhu.edu/~hall/java/Java2D-Tutorial.html

case class Point2D[X, Y](x: X, y: Y)

class ScaledArea2D[X, Y](width: Int, height: Int, pad: Int,
  minX: X, maxX: X, minY: Y, maxY: Y)(
    implicit xPortionable: Portionable[X], yPortionable: Portionable[Y]) {

  val drawableWidth = width - (2 * pad)

  val drawableHeight = height - (2 * pad)

  def framePoint(sp: Point2D[X, Y]) = new Point(
    pad + (drawableWidth * xPortionable.portion(minX, sp.x, maxX)).toInt,
    height - pad - (drawableHeight * yPortionable.portion(minY, sp.y, maxY)).toInt
  )

  def fillOval(g2d: Graphics2D, p: Point2D[X, Y], width: Int, height: Int): Unit = {
    val fp = framePoint(p)
    g2d.fillOval(fp.x - width / 2, fp.y - height / 2, width, height)
  }

  def drawOval(g2d: Graphics2D, p: Point2D[X, Y], width: Int, height: Int): Unit = {
    val fp = framePoint(p)
    g2d.drawOval(fp.x - width / 2, fp.y - height / 2, width, height)
  }

  def horizontalLine(g2d: Graphics2D, y: Y): Unit = drawLine(g2d, Point2D(minX, y), Point2D(maxX, y))

  def verticalLine(g2d: Graphics2D, x: X): Unit = drawLine(g2d, Point2D(x, minY), Point2D(x, maxY))

  def drawLine(g2d: Graphics2D, p0: Point2D[X, Y], p1: Point2D[X, Y]): Unit = {
    val fp0 = framePoint(p0)
    val fp1 = framePoint(p1)
    g2d.drawLine(fp0.x, fp0.y, fp1.x, fp1.y)
  }

  def fillRectangle(g2d: Graphics2D, p0: Point2D[X, Y], p1: Point2D[X, Y]): Unit = {
    val fp0 = framePoint(p0)
    val fp1 = framePoint(p1)
    g2d.fillRect(min(fp0.x, fp1.x), min(fp0.y, fp1.y), abs(fp0.x - fp1.x), abs(fp0.y - fp1.y))
  }

  def drawRectangle(g2d: Graphics2D, p0: Point2D[X, Y], p1: Point2D[X, Y]): Unit = {
    val fp0 = framePoint(p0)
    val fp1 = framePoint(p1)
    g2d.drawRect(min(fp0.x, fp1.x), min(fp0.y, fp1.y), abs(fp0.x - fp1.x), abs(fp0.y - fp1.y))
  }

  def drawString(g2d: Graphics2D, s: String, p: Point2D[X, Y]): Unit = {
    val fp = framePoint(p)
    g2d.drawString(s, fp.x, fp.y)
  }

  def drawStringAtAngle(g2d: Graphics2D, fontMetrics: FontMetrics, s: String, p: Point2D[X, Y], angle: Angle.Q): Unit = {
    val fp = framePoint(p)
    val a = (angle in rad).magnitude.doubleValue
    g2d.translate(fp.x, fp.y + fontMetrics.getHeight)
    g2d.rotate(a)
    g2d.drawString(s, 0, 0)
    g2d.rotate(-1 * a)
    g2d.translate(-fp.x, -fp.y - fontMetrics.getHeight)
  }
  
  val zeroDegrees = 0.0 *: °
  
  def drawXTic(g2d: Graphics2D, fontMetrics: FontMetrics, xTic: (X, String), fDrawLine: Boolean, angle: Angle.Q = zeroDegrees): Unit = {
    val (x, label) = xTic
    if (fDrawLine) {
      g2d.setColor(Color.lightGray)
      drawLine(g2d, Point2D(x, minY), Point2D(x, maxY))
    }
    val bottomScaled = Point2D(x, minY)
    val bottomUnscaled = framePoint(bottomScaled)
    g2d.setColor(Color.black)
    // TODO: angle xtics?
    if( angle == 0.0)
      g2d.drawString(label, bottomUnscaled.x - fontMetrics.stringWidth(label) / 2, bottomUnscaled.y + fontMetrics.getHeight)
    else
      drawStringAtAngle(g2d, fontMetrics, label, bottomScaled, angle)
    g2d.drawLine(bottomUnscaled.x, bottomUnscaled.y - 2, bottomUnscaled.x, bottomUnscaled.y + 2)
  }

  def drawXTics(g2d: Graphics2D, fontMetrics: FontMetrics, xTics: Seq[(X, String)], fDrawLines: Boolean=true, angle: Angle.Q=zeroDegrees): Unit =
    xTics.map({
      case (x, label) => drawXTic(g2d: Graphics2D, fontMetrics, (x, label), fDrawLines, angle)
    })

  def drawYTics(g2d: Graphics2D, fontMetrics: FontMetrics, yTics: Seq[(Y, String)]): Unit = yTics.map({
    case (y, label) => {
      val leftScaled = Point2D(minX, y)
      val leftUnscaled = framePoint(leftScaled)
      g2d.setColor(Color.lightGray)
      drawLine(g2d, leftScaled, Point2D(maxX, y))
      g2d.setColor(Color.black)
      g2d.drawString(label, leftUnscaled.x - fontMetrics.stringWidth(label) - 5, leftUnscaled.y + fontMetrics.getHeight / 2)
      g2d.drawLine(leftUnscaled.x - 2, leftUnscaled.y, leftUnscaled.x + 2, leftUnscaled.y)
    }
  })

}
