package axle.visualize.element

import axle.visualize.Color
import axle.visualize.Color.black
import axle.quanta.Angle
import axle.quanta.UnittedQuantity
import axle.visualize.ScaledArea2D
import axle.visualize.angleDouble

case class XTics[X, Y](
  scaledArea: ScaledArea2D[X, Y],
  tics:       Seq[(X, String)],
  fontName:   String,
  fontSize:   Double,
  bold:       Boolean                                = false,
  drawLines:  Boolean                                = true,
  angle:      Option[UnittedQuantity[Angle, Double]], // no labels when angle = None
  color:      Color                                  = black) {

  val zeroDegrees = 0d *: angleDouble.degree

}
