package axle.visualize

import javax.swing.JPanel
import java.awt.Color._
import java.awt.Font
import java.awt.FontMetrics
import java.awt.Graphics
import java.awt.Graphics2D
import math.Pi
import axle.quanta._
import Angle._
import Plottable._

class BarChartComponent[X, S, Y](barChart: BarChart[X, S, Y]) extends JPanel {

  import barChart._

  setMinimumSize(new java.awt.Dimension(width, height))
  
//  val clockwise90 = Pi / -2.0
//  val counterClockwise90 = -1.0 * clockwise90
//  val clockwise360 = Pi * 2

  val keyLeftPadding = 20
  val keyTopPadding = 50
  val keyWidth = 80
  
  val colors = List(blue, red, green, orange, pink, yellow)

  val colorStream = Stream.continually(colors.toStream).flatten

  val minX = 0.0
  val maxX = 1.0
  val yAxis = minX

  val padding = 0.05 // on each side
  val widthPerX = (1.0 - (2 * padding)) / xs.size
  val whiteSpace = widthPerX * (1.0 - barWidthPercent)

  val scaledArea = new ScaledArea2D(
    width = if (drawKey) width - (keyWidth + keyLeftPadding) else width,
    height,
    border,
    minX, maxX, minY, maxY
  )(DoublePlottable, yPlottable())

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
      val ty = height + fontMetrics.stringWidth(text) / 2
      g2d.translate(tx, ty)
      g2d.rotate(twist)
      g2d.drawString(text, 0, 0)
      g2d.rotate(-1 * twist)
      g2d.translate(-tx, -ty)
    })

  }

  def key(g2d: Graphics2D): Unit = {
    val lineHeight = g2d.getFontMetrics.getHeight
    for (((s, j), color) <- ss.zipWithIndex.zip(colorStream)) {
      g2d.setColor(color)
      g2d.drawString(sLabeller(s), width - keyWidth, keyTopPadding + lineHeight * (j+1))
    }
  }

  override def paintComponent(g: Graphics): Unit = {

    val g2d = g.asInstanceOf[Graphics2D]
    val fontMetrics = g2d.getFontMetrics

    g2d.setColor(black)
    labels(g2d, fontMetrics)
    scaledArea.verticalLine(g2d, yAxis)
    scaledArea.horizontalLine(g2d, xAxis)

    scaledArea.drawYTics(g2d, fontMetrics, yTics)

    val xTics = xs.zipWithIndex.map({
      case (x, i) => (padding + (i + 0.5) * widthPerX, xLabeller(x))
    }).toList
    scaledArea.drawXTics(g2d, fontMetrics, xTics, false, -36 *: °)

    val barSliceWidth = (widthPerX - (whiteSpace / 2.0)) / ss.size.toDouble

    for (((s, j), color) <- ss.zipWithIndex.zip(colorStream)) {
      g2d.setColor(color)
      for ((x, i) <- xs.zipWithIndex) {
        val leftX = padding + (whiteSpace / 2.0) + i * widthPerX + j * barSliceWidth
        val rightX = leftX + barSliceWidth
        scaledArea.fillRectangle(g2d, Point2D(leftX, minY), Point2D(rightX, y(x, s)))
      }
    }

    if (drawKey) {
      key(g2d)
    }

  }

}
