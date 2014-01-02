package axle.visualize.element

import axle.visualize._
import java.awt.Graphics2D
import java.awt.Color

class VerticalLine[X, Y](
  scaledArea: ScaledArea2D[X, Y],
  v: X,
  color: Color = Color.black) extends Paintable {

  import scaledArea._

  def paint(g2d: Graphics2D): Unit = {
    g2d.setColor(color)
    drawLine(g2d, Point2D(v, minY), Point2D(v, maxY))
  }

}
