package axle.visualize

import java.awt.{ Dimension, BasicStroke, Color, Paint, Stroke, Insets, Graphics, Graphics2D, Point }
import javax.swing.JPanel
import java.awt.event.MouseEvent

import collection._

class Plot[X, DX, Y, DY](
  xys: Seq[SortedMap[X, Y]],
  xDiff: (X, X) => DX, xDiv: (DX, DX) => Double, xCmp: Ordering[X],
  yDiff: (Y, Y) => DY, yDiv: (DY, DY) => Double, yCmp: Ordering[Y],
  connect: Boolean = true) extends JPanel {

  val PAD = 50
  val WIDTH = 600
  val HEIGHT = 600
  val DIAMETER = 4

  val colors = List(Color.blue, Color.red, Color.green, Color.orange, Color.pink, Color.yellow)

  val colorStream = Stream.continually(colors.toStream).flatten

  val minX = xys.map(_.firstKey).min(xCmp)
  val maxX = xys.map(_.lastKey).max(xCmp)
  val minY = xys.map(_.values.min(yCmp)).min(yCmp)
  val maxY = xys.map(_.values.max(yCmp)).max(yCmp)

  val scaledArea = new ScaledArea2D(
    WIDTH, HEIGHT, PAD,
    minX, maxX, xDiff, xDiv,
    minY, maxY, yDiff, yDiv)

  override def paintComponent(g: Graphics): Unit = {
    val size = getSize()
    val g2d = g.asInstanceOf[Graphics2D]
    for ((xy, color) <- xys.zip(colorStream)) {
      g2d.setColor(color)
      if (connect) {
        val xsStream = xy.keysIterator.toStream
        for ((x0, x1) <- xsStream.zip(xsStream.tail)) {
          scaledArea.drawLine(g2d, Point2D(x0, xy(x0)), Point2D(x1, xy(x1)))
        }
      }
      for (x <- xy.keys) {
        scaledArea.fillOval(g2d, Point2D(x, xy(x)), DIAMETER, DIAMETER)
      }
    }
  }

}
