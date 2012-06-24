package axle.visualize

import java.awt.{ Dimension, BasicStroke, Color, Paint, Stroke, Insets, Graphics, Graphics2D, Point }
import javax.swing.JPanel
import java.awt.event.MouseEvent
import collection._

class Plot[X, DX, Y, DY](fs: Seq[SortedMap[X, Y]], connect: Boolean = true,
  width: Int = 600, height: Int = 600,
  border: Int = 50, pointDiameter: Int = 4,
  title: Option[String] = None, xAxisLabel: Option[String] = None, yAxisLabel: Option[String] = None)(
    implicit xPlottable: Plottable[X], yPlottable: Plottable[Y]) extends JPanel {

  val colors = List(Color.blue, Color.red, Color.green, Color.orange, Color.pink, Color.yellow)

  val colorStream = Stream.continually(colors.toStream).flatten

  val clockwise90 = math.Pi / -2.0
  val counterClockwise90 = -1.0 * clockwise90

  val minX = fs.map(_.firstKey).min(xPlottable)
  val maxX = fs.map(_.lastKey).max(xPlottable)
  val minY = fs.map(_.values.min(yPlottable)).min(yPlottable)
  val maxY = fs.map(_.values.max(yPlottable)).max(yPlottable)

  val scaledArea = new ScaledArea2D(width, height, border, minX, maxX, minY, maxY)

  override def paintComponent(g: Graphics): Unit = {
    // val size = getSize()
    val g2d = g.asInstanceOf[Graphics2D]
    val fontMetrics = g2d.getFontMetrics

    title.map(text =>
      g2d.drawString(text, (width - fontMetrics.stringWidth(text)) / 2, 20)
    )

    xAxisLabel.map(text =>
      g2d.drawString(text, (width - fontMetrics.stringWidth(text)) / 2, height - 20)
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

    // yAxisLabel.map(_.zipWithIndex.map({ case (c: Char, i: Int) => g2d.drawString(c.toString, 20, height / 2 + i * 10) }))
    for ((f, color) <- fs.zip(colorStream)) {
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
  }

}
