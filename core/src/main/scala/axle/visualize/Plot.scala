package axle.visualize

import java.awt.{ Dimension, BasicStroke, Color, Paint, Stroke, Insets, Graphics, Graphics2D, Point }
import javax.swing.JPanel
import java.awt.event.MouseEvent
import collection._

class Plot[X, DX, Y, DY](fs: Seq[SortedMap[X, Y]], connect: Boolean = true)(
  implicit xPlottable: Plottable[X], yPlottable: Plottable[Y]) extends JPanel {

  val PAD = 50
  val WIDTH = 600
  val HEIGHT = 600
  val DIAMETER = 4

  val colors = List(Color.blue, Color.red, Color.green, Color.orange, Color.pink, Color.yellow)

  val colorStream = Stream.continually(colors.toStream).flatten

  val minX = fs.map(_.firstKey).min(xPlottable)
  val maxX = fs.map(_.lastKey).max(xPlottable)
  val minY = fs.map(_.values.min(yPlottable)).min(yPlottable)
  val maxY = fs.map(_.values.max(yPlottable)).max(yPlottable)

  val scaledArea = new ScaledArea2D(WIDTH, HEIGHT, PAD, minX, maxX, minY, maxY)

  override def paintComponent(g: Graphics): Unit = {
    val size = getSize()
    val g2d = g.asInstanceOf[Graphics2D]
    for ((f, color) <- fs.zip(colorStream)) {
      g2d.setColor(color)
      if (connect) {
        val xsStream = f.keysIterator.toStream
        for ((x0, x1) <- xsStream.zip(xsStream.tail)) {
          scaledArea.drawLine(g2d, Point2D(x0, f(x0)), Point2D(x1, f(x1)))
        }
      }
      for (x <- f.keys) {
        scaledArea.fillOval(g2d, Point2D(x, f(x)), DIAMETER, DIAMETER)
      }
    }
  }

}
