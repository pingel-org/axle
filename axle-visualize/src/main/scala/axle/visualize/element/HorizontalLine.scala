package axle.visualize.element

import axle.visualize._
import java.awt.Color
import java.awt.Graphics2D

class HorizontalLine[X, Y](
  scaledArea: ScaledArea2D[X, Y],
  h: Y,
  color: Color = Color.black) extends Paintable {

  import scaledArea._

  def paint(g2d: Graphics2D): Unit = {
    g2d.setColor(color)
    drawLine(g2d, Point2D(minX, h), Point2D(maxX, h))
  }
}
