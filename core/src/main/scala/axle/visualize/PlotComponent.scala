package axle.visualize

import javax.swing.JPanel
import java.awt.Color._
import java.awt.Font
import java.awt.FontMetrics
import java.awt.Graphics
import java.awt.Graphics2D
import axle.quanta.Angle._

class PlotComponent[X, Y](plot: Plot[X, Y]) extends JPanel {

  import plot._

  setMinimumSize(new java.awt.Dimension(width, height))

  val keyLeftPadding = 20
  val keyTopPadding = 50
  val keyWidth = 80

  val colors = List(blue, red, green, orange, pink, yellow)

  val colorStream = Stream.continually(colors.toStream).flatten

  val scaledArea = new ScaledArea2D(
    width = if (drawKey) width - (keyWidth + keyLeftPadding) else width,
    height, border,
    minX, maxX, minY, maxY
  )(xPlottable(), yPlottable())

  val normalFont = new Font("Courier New", Font.BOLD, 12)
  val titleFont = new Font("Palatino", Font.BOLD, 20)

  val twist = (clockwise90 in rad).magnitude.doubleValue
  
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
      g2d.rotate(twist)
      g2d.drawString(text, 0, 0)
      g2d.rotate(-1 * twist)
      g2d.translate(-tx, -ty)
    })

  }

  def key(g2d: Graphics2D): Unit = {
    val lineHeight = g2d.getFontMetrics.getHeight
    for ((((label, f), color), i) <- lfs.zip(colorStream).zipWithIndex) {
      g2d.setColor(color)
      g2d.drawString(label, width - keyWidth, keyTopPadding + lineHeight * (i + 1))
    }
  }

  override def paintComponent(g: Graphics): Unit = {

    val g2d = g.asInstanceOf[Graphics2D]
    val fontMetrics = g2d.getFontMetrics

    g2d.setColor(black)
    labels(g2d, fontMetrics)
    scaledArea.verticalLine(g2d, yAxis)
    scaledArea.horizontalLine(g2d, xAxis)

    scaledArea.drawXTics(g2d, fontMetrics, xTics)
    scaledArea.drawYTics(g2d, fontMetrics, yTics)

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
