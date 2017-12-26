package axle.visualize.element

import axle.visualize.Color
import axle.visualize.Color.black
import axle.quanta.Angle
import axle.quanta.UnittedQuantity
import axle.visualize.angleDouble
import spire.implicits.DoubleAlgebra

case class Text(
  text:     String,
  x:        Double,
  y:        Double,
  fontName: String,
  fontSize: Double,
  bold:     Boolean                                = false,
  centered: Boolean                                = true,
  color:    Color                                  = black,
  angle:    Option[UnittedQuantity[Angle, Double]] = None) {

  val angleRadOpt = angle.map(a => (a in angleDouble.radian).magnitude)

}
