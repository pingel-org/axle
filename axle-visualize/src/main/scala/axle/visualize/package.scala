package axle

import axle.quanta.Angle

import spire.algebra._
import axle.algebra.DirectedGraph
import axle.jung.JungDirectedGraph
import spire.implicits.DoubleAlgebra

package object visualize {

  val white = Color(r = 255, g = 255, b = 255)
  val lightGray = Color(r = 192, g = 192, b = 192)
  val darkGray = Color(r = 64, g = 64, b = 64)
  val black = Color(r = 0, g = 0, b = 0)
  val blue = Color(r = 0, g = 0, b = 255)
  val red = Color(r = 255, g = 0, b = 0)
  val green = Color(r = 0, g = 255, b = 0)
  val orange = Color(r = 255, g = 200, b = 0)
  val pink = Color(r = 255, g = 175, b = 175)
  val yellow = Color(r = 255, g = 255, b = 0)

  val defaultColors = List(blue, red, green, orange, pink, yellow)

  // angleDouble is here for visualizations that take an angle.  For example: BarChart's label angle
  implicit val angleDouble = Angle.converterGraph[Double, JungDirectedGraph](
    Field[Double],
    Eq[Double],
    DirectedGraph[JungDirectedGraph],
    axle.algebra.modules.doubleDoubleModule,
    axle.algebra.modules.doubleRationalModule)

}