package axle.visualize.element

import java.awt.Color

import axle.quanta.Angle
import axle.quanta.UnittedQuantity
import axle.visualize.ScaledArea2D
import axle.visualize.angleDouble

case class XTics[X, Y](
    scaledArea: ScaledArea2D[X, Y],
    tics: Seq[(X, String)],
    fontName: String,
    fontSize: Int,
    bold: Boolean = false,
    drawLines: Boolean = true,
    angle: UnittedQuantity[Angle, Double],
    color: Color = Color.black) {

  val zeroDegrees = 0d *: angleDouble.degree

}
