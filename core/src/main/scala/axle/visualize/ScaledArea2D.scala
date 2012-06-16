package axle.visualize

import java.awt.{ Dimension, BasicStroke, Color, Paint, Stroke, Insets, Graphics, Graphics2D, Point }
import javax.swing.JPanel
import java.awt.event.MouseEvent

// http://www.apl.jhu.edu/~hall/java/Java2D-Tutorial.html

case class Point2D[X, Y](x: X, y: Y)

class ScaledArea2D[X, DX, Y, DY](
  width: Int, height: Int, pad: Int,
  minX: X, maxX: X, xDiff: (X, X) => DX, xDiv: (DX, DX) => Double,
  minY: Y, maxY: Y, yDiff: (Y, Y) => DY, yDiv: (DY, DY) => Double) {

  val drawableWidth = width - (2 * pad)
  def xScaleRange = xDiff(maxX, minX)

  val drawableHeight = height - (2 * pad)
  def yScaleRange = yDiff(maxY, minY)

  def framePoint(sp: Point2D[X, Y]) = new Point(
    pad + (drawableWidth * xDiv(xDiff(sp.x, minX), xScaleRange)).toInt,
    height - pad - (drawableHeight * yDiv(yDiff(sp.y, minY), yScaleRange)).toInt
  )

  def fillOval(g2d: Graphics2D, p: Point2D[X, Y], width: Int, height: Int) = {
    val fp = framePoint(p)
    g2d.fillOval(fp.x - width / 2, fp.y - height / 2, width, height)
  }

  def drawOval(g2d: Graphics2D, p: Point2D[X, Y], width: Int, height: Int) = {
    val fp = framePoint(p)
    g2d.drawOval(fp.x - width / 2, fp.y - height / 2, width, height)
  }

  def drawLine(g2d: Graphics2D, p0: Point2D[X, Y], p1: Point2D[X, Y]) = {
    val fp0 = framePoint(p0)
    val fp1 = framePoint(p1)
    g2d.drawLine(fp0.x, fp0.y, fp1.x, fp1.y)
  }

  def drawRectangle(g2d: Graphics2D, p0: Point2D[X, Y], p1: Point2D[X, Y]) = {
    val fp0 = framePoint(p0)
    val fp1 = framePoint(p1)
    g2d.drawRect(math.min(fp0.x, fp1.x), math.min(fp0.y, fp1.y), math.abs(fp0.x - fp1.x), math.abs(fp0.y - fp1.y))
  }

  def drawString(g2d: Graphics2D, s: String, p: Point2D[X, Y]) = {
    val fp = framePoint(p)
    g2d.drawString(s, fp.x, fp.y)
  }

}
