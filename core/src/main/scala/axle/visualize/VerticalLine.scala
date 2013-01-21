package axle.visualize

import java.awt.Graphics2D
import java.awt.Color

class VerticalLine[X, Y](
  scaledArea: ScaledArea2D[X, Y],
  v: X,
  color: Color = Color.black) extends Paintable {

  def paint(g2d: Graphics2D): Unit = {
    g2d.setColor(color)
    scaledArea.verticalLine(g2d, v)
  }

}
