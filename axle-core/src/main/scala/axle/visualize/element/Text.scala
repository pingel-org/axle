package axle.visualize.element

import spire.algebra._

import axle.visualize.Color
import axle.visualize.Color.black
import axle.quanta.Angle
import axle.quanta.UnittedQuantity
import axle.visualize.angleDouble

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

  implicit val mmDouble: MultiplicativeMonoid[Double] = spire.implicits.DoubleAlgebra

  val angleRadOpt = angle.map(a => (a in angleDouble.radian).magnitude)

}
