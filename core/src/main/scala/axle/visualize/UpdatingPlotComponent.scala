package axle.visualize

import collection._
import javax.swing.JPanel
import java.awt.Color._
import java.awt.Font
import java.awt.FontMetrics
import java.awt.Graphics
import java.awt.Graphics2D
import axle.quanta.Angle._

class UpdatingPlotComponent[X, Y](plot: UpdatingPlot[X, Y]) extends JPanel {

  setMinimumSize(new java.awt.Dimension(plot.width, plot.height))

  val keyLeftPadding = 20
  val keyTopPadding = 50
  val keyWidth = 80

  val colors = List(blue, red, green, orange, pink, yellow)

  val colorStream = Stream.continually(colors.toStream).flatten

  val normalFont = new Font("Courier New", Font.BOLD, 12)
  val titleFont = new Font("Palatino", Font.BOLD, 20)

  val twist = (clockwise90 in rad).magnitude.doubleValue

  def labels(g2d: Graphics2D, fontMetrics: FontMetrics): Unit = {

    plot.title.map(text => {
      g2d.setFont(titleFont)
      g2d.drawString(text, (plot.width - fontMetrics.stringWidth(text)) / 2, 20)
    })

    g2d.setFont(normalFont)

    plot.xAxisLabel.map(text =>
      g2d.drawString(text, (plot.width - fontMetrics.stringWidth(text)) / 2, plot.height + (fontMetrics.getHeight - plot.border) / 2)
    )

    plot.yAxisLabel.map(text => {
      val tx = 20
      val ty = (plot.height + fontMetrics.stringWidth(text)) / 2
      g2d.translate(tx, ty)
      g2d.rotate(twist)
      g2d.drawString(text, 0, 0)
      g2d.rotate(-1 * twist)
      g2d.translate(-tx, -ty)
    })

  }

  def key(g2d: Graphics2D, data: Seq[(String, SortedMap[X, Y])]): Unit = {
    val lineHeight = g2d.getFontMetrics.getHeight
    for ((((label, f), color), i) <- data.zip(colorStream).zipWithIndex) {
      g2d.setColor(color)
      g2d.drawString(label, plot.width - keyWidth, keyTopPadding + lineHeight * (i + 1))
    }
  }

  def update() = {
    
  }
  
  override def paintComponent(g: Graphics): Unit = {

    val data = plot.dataSnapshot()

    val (minPoint, maxPoint, xTics, yTics) = plot.boundsAndTics(data)

    val scaledArea = new ScaledArea2D(
      width = if (plot.drawKey) plot.width - (keyWidth + keyLeftPadding) else plot.width,
      plot.height, plot.border,
      minPoint.x, maxPoint.x, minPoint.y, maxPoint.y
    )(plot.xPlottable, plot.yPlottable)

    val g2d = g.asInstanceOf[Graphics2D]
    val fontMetrics = g2d.getFontMetrics

    g2d.setColor(black)
    labels(g2d, fontMetrics)
    scaledArea.verticalLine(g2d, plot.yAxis)
    scaledArea.horizontalLine(g2d, plot.xAxis)

    scaledArea.drawXTics(g2d, fontMetrics, xTics)
    scaledArea.drawYTics(g2d, fontMetrics, yTics)

    for (((label, f), color) <- data.zip(colorStream)) {
      g2d.setColor(color)
      if (plot.connect) {
        val xsStream = f.keysIterator.toStream
        for ((x0, x1) <- xsStream.zip(xsStream.tail)) {
          scaledArea.drawLine(g2d, Point2D(x0, f(x0)), Point2D(x1, f(x1)))
        }
      }
      for (x <- f.keys) {
        scaledArea.fillOval(g2d, Point2D(x, f(x)), plot.pointDiameter, plot.pointDiameter)
      }
    }

    if (plot.drawKey) {
      key(g2d, data)
    }
  }

}
