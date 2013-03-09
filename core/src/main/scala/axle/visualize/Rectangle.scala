package axle.visualize

import java.awt.Color
import java.awt.Graphics2D

case class Rectangle[X, Y](scaledArea: ScaledArea2D[X, Y], lowerLeft: Point2D[X, Y], upperRight: Point2D[X, Y], color: java.awt.Color) {

  def paint(g2d: Graphics2D): Unit = {
    g2d.setColor(color)
    scaledArea.fillRectangle(
      g2d,
      Point2D(lowerLeft.x, lowerLeft.y),
      Point2D(upperRight.x, upperRight.y)
    )
  }

}
