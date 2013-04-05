package axle.visualize.element

import axle.visualize._
import java.awt.Color
import java.awt.Graphics2D

case class Rectangle[X, Y](
  scaledArea: ScaledArea2D[X, Y],
  lowerLeft: Point2D[X, Y],
  upperRight: Point2D[X, Y],
  fillColor: Option[Color] = None,
  borderColor: Option[Color] = None) {

  def paint(g2d: Graphics2D): Unit = {
    fillColor.map(color => {
      g2d.setColor(color)
      scaledArea.fillRectangle(
        g2d,
        Point2D(lowerLeft.x, lowerLeft.y),
        Point2D(upperRight.x, upperRight.y)
      )
    })
    borderColor.map(color => {
      g2d.setColor(color)
      scaledArea.drawRectangle(
        g2d,
        Point2D(lowerLeft.x, lowerLeft.y),
        Point2D(upperRight.x, upperRight.y)
      )
    })
  }

}
