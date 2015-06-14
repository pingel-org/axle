package axle.visualize.element

import java.awt.Color
import java.awt.Font

import axle.quanta.Angle
import axle.quanta.UnittedQuantity
import axle.visualize.angleDouble
import spire.implicits.DoubleAlgebra

case class Text(
    text: String,
    font: Font,
    x: Int,
    y: Int,
    centered: Boolean = true,
    color: Color = Color.black,
    angle: Option[UnittedQuantity[Angle, Double]] = None) {

  val angleRadOpt = angle.map(a => (a in angleDouble.radian).magnitude)

}
