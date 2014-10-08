package axle.visualize.element

import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D

import axle.algebra.Plottable
import axle.visualize.BarChart
import axle.visualize.BarChartGrouped
import axle.visualize.Paintable
import axle.visualize.Plot

class BarChartKey[S, Y: Plottable, D](chart: BarChart[S, Y, D], font: Font, colorStream: Stream[Color])
  extends Paintable {

  import chart._

  val slices = dataView.keys(initialValue)

  def paint(g2d: Graphics2D): Unit = {
    g2d.setFont(font)
    val lineHeight = g2d.getFontMetrics.getHeight
    slices.toVector.zipWithIndex.zip(colorStream) foreach {
      case ((s, j), color) =>
        g2d.setColor(color)
        g2d.drawString(sLabeller(s), width - keyWidth, keyTopPadding + lineHeight * (j + 1))
    }
  }

}

class BarChartGroupedKey[G, S, Y: Plottable, D](chart: BarChartGrouped[G, S, Y, D], font: Font, colorStream: Stream[Color]) extends Paintable {

  import chart._

  val slices = groupedDataView.slices(initialValue)

  def paint(g2d: Graphics2D): Unit = {
    g2d.setFont(font)
    val lineHeight = g2d.getFontMetrics.getHeight
    slices.toVector.zipWithIndex.zip(colorStream) foreach {
      case ((s, j), color) =>
        g2d.setColor(color)
        g2d.drawString(sLabeller(s), width - keyWidth, keyTopPadding + lineHeight * (j + 1))
    }
  }

}

class Key[X, Y, D](
  plot: Plot[X, Y, D],
  font: Font,
  colorStream: Stream[Color],
  width: Int,
  topPadding: Int,
  data: Seq[(String, D)]) extends Paintable {

  def paint(g2d: Graphics2D): Unit = {

    g2d.setFont(font)
    val fontMetrics = g2d.getFontMetrics

    val lineHeight = g2d.getFontMetrics.getHeight
    data.zip(colorStream).zipWithIndex foreach {
      case (((label, _), color), i) =>
        g2d.setColor(color)
        g2d.drawString(label, plot.width - width, topPadding + lineHeight * (i + 1))
    }
  }

}
