package axle.visualize

import java.awt.{ Dimension, BasicStroke, Color, Paint, Stroke, Insets, Graphics, Graphics2D, Point, FontMetrics, Font }
import javax.swing.JPanel
import java.awt.event.MouseEvent
import collection._
import math.{ pow, floor, log, abs }

class Plot[X, DX, Y, DY](
  lfs: Seq[(String, SortedMap[X, Y])],
  connect: Boolean = true,
  drawKey: Boolean = true,
  width: Int = 700,
  height: Int = 600,
  border: Int = 50,
  pointDiameter: Int = 4,
  title: Option[String] = None,
  xAxis: Y,
  xAxisLabel: Option[String] = None,
  yAxis: X,
  yAxisLabel: Option[String] = None)(
    implicit xPlottable: Plottable[X], yPlottable: Plottable[Y]) extends JPanel {

  val colors = List(Color.blue, Color.red, Color.green, Color.orange, Color.pink, Color.yellow)

  val colorStream = Stream.continually(colors.toStream).flatten

  val clockwise90 = math.Pi / -2.0
  val counterClockwise90 = -1.0 * clockwise90

  val minX = lfs.map(_._2.firstKey).min(xPlottable)
  val maxX = lfs.map(_._2.lastKey).max(xPlottable)
  val minY = lfs.map(_._2.values.min(yPlottable)).min(yPlottable)
  val maxY = lfs.map(_._2.values.max(yPlottable)).max(yPlottable)

  val xTics = xPlottable.tics(minX, maxX)
  val yTics = yPlottable.tics(minY, maxY)

  val scaledArea = new ScaledArea2D(width = width - 100, height, border, minX, maxX, minY, maxY)

  val normalFont = new Font("Courier New", Font.BOLD, 12)
  val titleFont = new Font("Palatino", Font.BOLD, 20)

  def labels(g2d: Graphics2D, fontMetrics: FontMetrics): Unit = {

    title.map(text => {
      g2d.setFont(titleFont)
      g2d.drawString(text, (width - fontMetrics.stringWidth(text)) / 2, 20)
    })

    g2d.setFont(normalFont)

    xAxisLabel.map(text =>
      g2d.drawString(text, (width - fontMetrics.stringWidth(text)) / 2, height + (fontMetrics.getHeight - border) / 2)
    )

    yAxisLabel.map(text => {
      val tx = 20
      val ty = (height + fontMetrics.stringWidth(text)) / 2
      g2d.translate(tx, ty)
      g2d.rotate(clockwise90)
      g2d.drawString(text, 0, 0)
      g2d.rotate(counterClockwise90)
      g2d.translate(-tx, -ty)
    })

  }

  def key(g2d: Graphics2D): Unit = {
    val lineHeight = g2d.getFontMetrics.getHeight
    for ((((label, f), color), i) <- lfs.zip(colorStream).zipWithIndex) {
      g2d.setColor(color)
      g2d.drawString(label, 620, 50 + lineHeight * i) // TODO embed position
    }
  }

  def drawXTics(g2d: Graphics2D, fontMetrics: FontMetrics): Unit = xTics.map({
    case (x, label) => {
      g2d.setColor(Color.lightGray)
      scaledArea.drawLine(g2d, Point2D(x, minY), Point2D(x, maxY))
    }
  })

  def drawYTics(g2d: Graphics2D, fontMetrics: FontMetrics): Unit = yTics.map({
    case (y, label) => {
      val leftScaled = Point2D(minX, y)
      val leftUnscaled = scaledArea.framePoint(leftScaled)
      g2d.setColor(Color.lightGray)
      scaledArea.drawLine(g2d, leftScaled, Point2D(maxX, y))
      g2d.setColor(Color.black)
      g2d.drawString(label, leftUnscaled.x - fontMetrics.stringWidth(label) - 2, leftUnscaled.y + fontMetrics.getHeight / 2)
      g2d.drawLine(leftUnscaled.x - 2, leftUnscaled.y, leftUnscaled.x + 2, leftUnscaled.y)
    }
  })

  override def paintComponent(g: Graphics): Unit = {

    val g2d = g.asInstanceOf[Graphics2D]
    val fontMetrics = g2d.getFontMetrics

    g2d.setColor(Color.black)
    labels(g2d, fontMetrics)
    scaledArea.verticalLine(g2d, yAxis)
    scaledArea.horizontalLine(g2d, xAxis)

    drawXTics(g2d, fontMetrics)
    drawYTics(g2d, fontMetrics)

    for (((label, f), color) <- lfs.zip(colorStream)) {
      g2d.setColor(color)
      if (connect) {
        val xsStream = f.keysIterator.toStream
        for ((x0, x1) <- xsStream.zip(xsStream.tail)) {
          scaledArea.drawLine(g2d, Point2D(x0, f(x0)), Point2D(x1, f(x1)))
        }
      }
      for (x <- f.keys) {
        scaledArea.fillOval(g2d, Point2D(x, f(x)), pointDiameter, pointDiameter)
      }
    }

    if (drawKey) {
      key(g2d)
    }
  }

}
