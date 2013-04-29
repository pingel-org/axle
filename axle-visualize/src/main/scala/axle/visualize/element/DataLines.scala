package axle.visualize.element

import axle.visualize._
import collection._
import java.awt.Color
import java.awt.Graphics2D

class DataLines[X, Y](
  scaledArea: ScaledArea2D[X, Y],
  data: Seq[(String, SortedMap[X, Y])],
  colorStream: Stream[Color],
  pointDiameter: Int,
  connect: Boolean = true) extends Paintable {

  def paint(g2d: Graphics2D): Unit = {

    for (((label, f), color) <- data.zip(colorStream)) {
      g2d.setColor(color)
      if (connect) {
        val xsStream = f.keysIterator.toStream
        for ((x0, x1) <- xsStream.zip(xsStream.tail)) {
          scaledArea.drawLine(g2d, Point2D(x0, f(x0)), Point2D(x1, f(x1)))
        }
      }
      if (pointDiameter > 0) {
        for (x <- f.keys) {
          scaledArea.fillOval(g2d, Point2D(x, f(x)), pointDiameter, pointDiameter)
        }
      }
    }
  }

}
