package axle.visualize.element

import java.awt.Color
import java.awt.Graphics2D

import axle.visualize.Paintable
import axle.visualize.Point2D
import axle.visualize.ScaledArea2D

case class VerticalLine[X, Y](
  scaledArea: ScaledArea2D[X, Y],
  v: X,
  color: Color = Color.black) extends Paintable {

  import scaledArea._

  def paint(g2d: Graphics2D): Unit = {
    g2d.setColor(color)
    drawLine(g2d, Point2D(v, minY), Point2D(v, maxY))
  }

}
