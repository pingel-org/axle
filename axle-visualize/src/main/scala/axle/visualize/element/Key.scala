package axle.visualize.element

import axle.visualize._
import java.awt.Graphics2D
import java.awt.Color
import java.awt.Font
import collection.immutable.SortedMap

import axle.algebra.Plottable

class BarChartKey[S, Y: Plottable, D](chart: BarChart[S, Y, D], font: Font, colorStream: Stream[Color]) extends Paintable {

  import chart._

  def paint(g2d: Graphics2D): Unit = {
    g2d.setFont(font)
    val lineHeight = g2d.getFontMetrics.getHeight
    slices.zipWithIndex.zip(colorStream) foreach { case ((s, j), color) =>
      g2d.setColor(color)
      g2d.drawString(sLabeller(s), width - keyWidth, keyTopPadding + lineHeight * (j + 1))
    }
  }

}

class BarChartGroupedKey[G, S, Y: Plottable](chart: BarChartGrouped[G, S, Y], font: Font, colorStream: Stream[Color]) extends Paintable {

  import chart._

  def paint(g2d: Graphics2D): Unit = {
    g2d.setFont(font)
    val lineHeight = g2d.getFontMetrics.getHeight
    slices.zipWithIndex.zip(colorStream) foreach { case ((s, j), color) =>
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
    data.zip(colorStream).zipWithIndex foreach { case (((label, _), color), i) =>
      g2d.setColor(color)
      g2d.drawString(label, plot.width - width, topPadding + lineHeight * (i + 1))
    }
  }

}
