package axle.visualize.element

import axle.visualize._
import java.awt.Color
import java.awt.Graphics2D
import axle.quanta._

class XTics[X, Y](
  scaledArea: ScaledArea2D[X, Y],
  tics: Seq[(X, String)],
  fDrawLines: Boolean = true,
  angle: Angle.Q,
  color: Color = Color.black) extends Paintable {

  def paint(g2d: Graphics2D): Unit = {
    g2d.setColor(color)
    val fontMetrics = g2d.getFontMetrics
    scaledArea.drawXTics(g2d, fontMetrics, tics, fDrawLines, angle)
  }

}
