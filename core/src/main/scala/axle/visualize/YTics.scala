package axle.visualize

import java.awt.Color
import java.awt.Graphics2D

class YTics[X, Y](
  scaledArea: ScaledArea2D[X, Y],
  tics: Seq[(Y, String)],
  color: Color = Color.black) extends Paintable {

  def paint(g2d: Graphics2D): Unit = {
    g2d.setColor(color)
    val fontMetrics = g2d.getFontMetrics
    scaledArea.drawYTics(g2d, fontMetrics, tics)
  }

}
