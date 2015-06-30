package axle.visualize.element

import java.awt.Color

import axle.quanta.Angle
import axle.quanta.UnittedQuantity
import axle.visualize.angleDouble
import spire.implicits.DoubleAlgebra

case class Text(
    text: String,
    x: Int,
    y: Int,
    fontName: String,
    fontSize: Int,
    bold: Boolean = false,
    centered: Boolean = true,
    color: Color = Color.black,
    angle: Option[UnittedQuantity[Angle, Double]] = None) {

  val angleRadOpt = angle.map(a => (a in angleDouble.radian).magnitude)

}
