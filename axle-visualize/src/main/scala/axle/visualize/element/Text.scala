package axle.visualize.element

import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D

import axle.quanta.Angle
import axle.quanta.Angle.rad
import axle.quanta.UnittedQuantity
import axle.visualize.Paintable
import spire.implicits.DoubleAlgebra 

class Text(
  text: String,
  font: Font,
  x: Int,
  y: Int,
  centered: Boolean = true,
  color: Color = Color.black,
  angle: Option[UnittedQuantity[Angle, Double]] = None) extends Paintable {

  val angleRadOpt = angle.map(a => (a in rad[Double]).magnitude)

  def paint(g2d: Graphics2D): Unit = {

    g2d.setColor(color)
    g2d.setFont(font)
    val fontMetrics = g2d.getFontMetrics

    if (angleRadOpt.isDefined) {
      val twist = angleRadOpt.get
      g2d.translate(x, y)
      g2d.rotate(-1 * twist)
      if (centered) {
        g2d.drawString(text, -fontMetrics.stringWidth(text) / 2, 0)
      } else {
        g2d.drawString(text, 0, 0)
      }
      g2d.rotate(twist)
      g2d.translate(-x, -y)
    } else {
      if (centered) {
        g2d.drawString(text, x - fontMetrics.stringWidth(text) / 2, y)
      } else {
        g2d.drawString(text, x, y)
      }
    }

  }

}
