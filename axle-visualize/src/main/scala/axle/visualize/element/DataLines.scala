package axle.visualize.element

import java.awt.Color
import java.awt.Graphics2D

import scala.collection.immutable.SortedMap

import axle.visualize.Paintable
import axle.visualize.Point2D
import axle.visualize.ScaledArea2D

class DataLines[X, Y](
  scaledArea: ScaledArea2D[X, Y],
  data: Seq[(String, SortedMap[X, Y])],
  colorStream: Stream[Color],
  pointDiameter: Int,
  connect: Boolean = true) extends Paintable {

  def paint(g2d: Graphics2D): Unit = {

    data.zip(colorStream) foreach {
      case (((label, f), color)) =>
        g2d.setColor(color)
        if (connect && f.size > 1) {
          val xsStream = f.keysIterator.toStream
          xsStream.zip(xsStream.tail) foreach {
            case (x0, x1) =>
              scaledArea.drawLine(g2d, Point2D(x0, f(x0)), Point2D(x1, f(x1)))
          }
        }
        if (pointDiameter > 0) {
          f.keys foreach { x =>
            scaledArea.fillOval(g2d, Point2D(x, f(x)), pointDiameter, pointDiameter)
          }
        }
    }
  }

}
