package axle.visualize

import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import axle.quanta._
import Angle._

class Text(
  text: String,
  font: Font,
  x: Int,
  y: Int,
  centered: Boolean = true,
  color: Color = Color.black,
  angle: Option[Angle.Q] = None) extends Paintable {

  val angleRadOpt = angle.map(a => (a in rad).magnitude.doubleValue)

  def paint(g2d: Graphics2D): Unit = {

    g2d.setColor(color)
    val fontMetrics = g2d.getFontMetrics
    g2d.setFont(font)

    if (angleRadOpt.isDefined) {
      // TODO: handle centered, angled text
      val twist = angleRadOpt.get
      g2d.translate(x, y)
      g2d.rotate(-1 * twist)
      g2d.drawString(text, 0, 0)
      g2d.rotate(twist)
      g2d.translate(-x, -y)
    } else {
      if (centered)
        g2d.drawString(text, x - fontMetrics.stringWidth(text) / 2, y)
      else
        g2d.drawString(text, x, y)
    }

  }

}
