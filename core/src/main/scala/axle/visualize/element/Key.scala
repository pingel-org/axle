package axle.visualize.element

import axle.visualize._
import collection._
import java.awt.Graphics2D
import java.awt.Color
import java.awt.Font

class BarChartKey[X, S, Y: Plottable](chart: BarChart[X, S, Y], font: Font, colorStream: Stream[Color]) extends Paintable {

  import chart._
  
  def paint(g2d: Graphics2D): Unit = {
    g2d.setFont(font)
    val lineHeight = g2d.getFontMetrics.getHeight
    for (((s, j), color) <- ss.zipWithIndex.zip(colorStream)) {
      g2d.setColor(color)
      g2d.drawString(sLabeller(s), width - keyWidth, keyTopPadding + lineHeight * (j + 1))
    }
  }
  
}

class Key[X, Y](
  plot: Plot[X, Y],
  font: Font,
  colorStream: Stream[Color],
  width: Int,
  topPadding: Int,
  data: Seq[(String, SortedMap[X, Y])]) extends Paintable {

  def paint(g2d: Graphics2D): Unit = {

    g2d.setFont(font)
    val fontMetrics = g2d.getFontMetrics

    val lineHeight = g2d.getFontMetrics.getHeight
    for ((((label, f), color), i) <- data.zip(colorStream).zipWithIndex) {
      g2d.setColor(color)
      g2d.drawString(label, plot.width - width, topPadding + lineHeight * (i + 1))
    }
  }

}
