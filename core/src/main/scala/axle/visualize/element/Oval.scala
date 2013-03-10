package axle.visualize.element

import axle.visualize._
import java.awt.Color
import java.awt.Graphics2D

case class Oval[X, Y](scaledArea: ScaledArea2D[X, Y], center: Point2D[X, Y], width: Int, height: Int, color: Color, borderColor: Color) {

  import scaledArea._

  def paint(g2d: Graphics2D): Unit = {
    g2d.setColor(borderColor)
    fillOval(g2d, center, width, height)
    g2d.setColor(color)
    drawOval(g2d, center, width, height)
  }

}
