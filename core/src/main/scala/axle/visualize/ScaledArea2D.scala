package axle.visualize

import java.awt.{ Dimension, BasicStroke, Color, Paint, Stroke, Insets, Graphics, Graphics2D, Point }
import javax.swing.JPanel
import java.awt.event.MouseEvent

case class PointDouble(x: Double, y: Double)

// http://www.apl.jhu.edu/~hall/java/Java2D-Tutorial.html

class ScaledArea2D(width: Int, height: Int, pad: Int, minX: Double, maxX: Double, minY: Double, maxY: Double) {

  val drawableWidth = width - (2 * pad)
  def xScaleRange = maxX - minX

  val drawableHeight = height - (2 * pad)
  def yScaleRange = maxY - minY

  def framePoint(sp: PointDouble) = new Point(
    pad + (drawableWidth * (sp.x / xScaleRange)).toInt,
    height - pad - (drawableHeight * (sp.y / yScaleRange)).toInt
  )

  def fillOval(g2d: Graphics2D, p: PointDouble, width: Int, height: Int) = {
    val fp = framePoint(p)
    g2d.fillOval(fp.x - width / 2, fp.y - height / 2, width, height)
  }

  def drawOval(g2d: Graphics2D, p: PointDouble, width: Int, height: Int) = {
    val fp = framePoint(p)
    g2d.drawOval(fp.x - width / 2, fp.y - height / 2, width, height)
  }

  def drawLine(g2d: Graphics2D, p0: PointDouble, p1: PointDouble) = {
    val fp0 = framePoint(p0)
    val fp1 = framePoint(p1)
    g2d.drawLine(fp0.x, fp0.y, fp1.x, fp1.y)
  }

  def drawRectangle(g2d: Graphics2D, p0: PointDouble, p1: PointDouble) = {
    val fp0 = framePoint(p0)
    val fp1 = framePoint(p1)
    g2d.drawRect(math.min(fp0.x, fp1.x), math.min(fp0.y, fp1.y), math.abs(fp0.x - fp1.x), math.abs(fp0.y - fp1.y))
  }

  def drawString(g2d: Graphics2D, s: String, p: PointDouble) = {
    val fp = framePoint(p)
    g2d.drawString(s, fp.x, fp.y)
  }

}
