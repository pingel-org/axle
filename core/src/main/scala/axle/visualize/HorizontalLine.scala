package axle.visualize

import java.awt.Color
import java.awt.Graphics2D

class HorizontalLine[X, Y](
  scaledArea: ScaledArea2D[X, Y],
  h: Y,
  color: Color = Color.black) extends Paintable {

  def paint(g2d: Graphics2D): Unit = {
    g2d.setColor(color)
    scaledArea.horizontalLine(g2d, h)
  }
}
