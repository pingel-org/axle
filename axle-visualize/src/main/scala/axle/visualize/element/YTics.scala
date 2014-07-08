package axle.visualize.element

import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D

import axle.visualize.Paintable
import axle.visualize.Point2D
import axle.visualize.ScaledArea2D

class YTics[X, Y](
  scaledArea: ScaledArea2D[X, Y],
  tics: Seq[(Y, String)],
  font: Font,
  color: Color = Color.black) extends Paintable {

  def paint(g2d: Graphics2D): Unit = {
    g2d.setColor(color)
    val fontMetrics = g2d.getFontMetrics
    import scaledArea._
    tics.map({
      case (y, label) => {
        val leftScaled = Point2D(minX, y)
        val leftUnscaled = framePoint(leftScaled)
        g2d.setColor(Color.lightGray)
        drawLine(g2d, leftScaled, Point2D(maxX, y))
        g2d.setColor(Color.black)
        g2d.drawString(label, leftUnscaled.x - fontMetrics.stringWidth(label) - 5, leftUnscaled.y + fontMetrics.getHeight / 2)
        g2d.drawLine(leftUnscaled.x - 2, leftUnscaled.y, leftUnscaled.x + 2, leftUnscaled.y)
      }
    })
  }

}
