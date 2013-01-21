package axle.visualize

import collection._
import java.awt.Graphics2D
import java.awt.Color

class Key[X, Y](
  plot: ReactivePlot[X, Y],
  colorStream: Stream[Color],
  width: Int,
  topPadding: Int,
  dataB: Behavior[Unit, Seq[(String, SortedMap[X, Y])]]) extends Paintable {

  def paint(g2d: Graphics2D): Unit = {

    val data = dataB.observe() // TODO: this should not happen here

    val fontMetrics = g2d.getFontMetrics

    val lineHeight = g2d.getFontMetrics.getHeight
    for ((((label, f), color), i) <- data.zip(colorStream).zipWithIndex) {
      g2d.setColor(color)
      g2d.drawString(label, plot.width - width, topPadding + lineHeight * (i + 1))
    }
  }

}
