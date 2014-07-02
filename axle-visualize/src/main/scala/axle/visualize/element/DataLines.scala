package axle.visualize.element

import java.awt.Color
import java.awt.Graphics2D

import scala.collection.immutable.SortedMap

import axle.visualize.Paintable
import axle.visualize.Point2D
import axle.visualize.ScaledArea2D

class DataLines[X, Y, D](
  scaledArea: ScaledArea2D[X, Y],
  data: Seq[(String, D)],
  orderedXs: D => IndexedSeq[X],
  x2y: (D, X) => Y,
  colorStream: Stream[Color],
  pointDiameter: Int,
  connect: Boolean = true) extends Paintable {

  def paint(g2d: Graphics2D): Unit = {

    data.zip(colorStream) foreach {
      case (((label, d), color)) =>
        g2d.setColor(color)
        val xs = orderedXs(d)
        if (connect && xs.size > 1) {
          val xsStream = xs.toStream
          xsStream.zip(xsStream.tail) foreach {
            case (x0, x1) =>
              scaledArea.drawLine(g2d, Point2D(x0, x2y(d, x0)), Point2D(x1, x2y(d, x1)))
          }
        }
        if (pointDiameter > 0) {
          xs foreach { x =>
            scaledArea.fillOval(g2d, Point2D(x, x2y(d, x)), pointDiameter, pointDiameter)
          }
        }
    }
  }

}
