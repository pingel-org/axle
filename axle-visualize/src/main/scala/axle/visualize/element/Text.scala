package axle.visualize.element

import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D

import axle.quanta.Angle
import axle.quanta.UnittedQuantity
import axle.quanta.UnitOfMeasurement
import axle.algebra.DirectedGraph
import axle.visualize.Paintable
import spire.implicits.DoubleAlgebra

case class Text[DG[_, _]: DirectedGraph](
  text: String,
  font: Font,
  x: Int,
  y: Int,
  centered: Boolean = true,
  color: Color = Color.black,
  angle: Option[UnittedQuantity[Angle, Double]] = None)(
    implicit angleCg: DG[UnitOfMeasurement[Angle, Double], Double => Double]) extends Paintable {

  val angleRadOpt = angle.map(a => (a in Angle.metadata[Double].radian).magnitude)

  def paint(g2d: Graphics2D): Unit = {

    g2d.setColor(color)
    g2d.setFont(font)
    val fontMetrics = g2d.getFontMetrics

    if (angleRadOpt.isDefined) {
      val twist = angleRadOpt.get
      g2d.translate(x, y)
      g2d.rotate(twist * -1)
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
