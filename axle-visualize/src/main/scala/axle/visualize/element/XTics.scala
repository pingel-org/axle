package axle.visualize.element

import java.awt.Color
import java.awt.Font
import java.awt.FontMetrics
import java.awt.Graphics2D

import spire.implicits.DoubleAlgebra
import axle.quanta.Angle
//import axle.quanta.Angle.eqTypeclass
import axle.quanta.Angle.{ ° => ° }
import axle.quanta.UnittedQuantity
import axle.visualize.Paintable
import axle.visualize.Point2D
import axle.visualize.ScaledArea2D
import spire.implicits.eqOps
import spire.math.Number.apply
import spire.implicits.moduleOps

class XTics[X, Y](
  scaledArea: ScaledArea2D[X, Y],
  tics: Seq[(X, String)],
  font: Font,
  fDrawLines: Boolean = true,
  angle: UnittedQuantity[Angle, Double],
  color: Color = Color.black) extends Paintable {

  def paint(g2d: Graphics2D): Unit = {
    g2d.setColor(color)
    g2d.setFont(font)
    val fontMetrics = g2d.getFontMetrics
    drawXTics(g2d, fontMetrics, tics, fDrawLines, angle)
  }

  import scaledArea._

  val zeroDegrees = 0d *: °[Double]

  def drawXTic(g2d: Graphics2D, fontMetrics: FontMetrics, xTic: (X, String), fDrawLine: Boolean, angle: UnittedQuantity[Angle, Double] = zeroDegrees): Unit = {

    val (x, label) = xTic
    if (fDrawLine) {
      g2d.setColor(Color.lightGray)
      drawLine(g2d, Point2D(x, minY), Point2D(x, maxY))
    }
    val bottomScaled = Point2D(x, minY)
    val bottomUnscaled = framePoint(bottomScaled)
    g2d.setColor(Color.black)

    // TODO: angle xtics?
    if (angle === zeroDegrees) {
      g2d.drawString(label, bottomUnscaled.x - fontMetrics.stringWidth(label) / 2, bottomUnscaled.y + fontMetrics.getHeight)
    } else {
      drawStringAtAngle(g2d, fontMetrics, label, bottomScaled, angle)
    }

    g2d.drawLine(bottomUnscaled.x, bottomUnscaled.y - 2, bottomUnscaled.x, bottomUnscaled.y + 2)
  }

  def drawXTics(g2d: Graphics2D, fontMetrics: FontMetrics, xTics: Seq[(X, String)], fDrawLines: Boolean = true, angle: UnittedQuantity[Angle, Double] = zeroDegrees): Unit =
    xTics.map({
      case (x, label) => drawXTic(g2d: Graphics2D, fontMetrics, (x, label), fDrawLines, angle)
    })

}
