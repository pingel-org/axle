package axle

import axle.algebra.DirectedGraph
import axle.jung.JungDirectedGraph
import axle.quanta.Angle
import axle.visualize.Color._
import spire.algebra.Eq
import spire.algebra.Field
import spire.implicits.DoubleAlgebra

package object visualize {

  val defaultColors = List(blue, red, green, orange, pink, yellow)

  // angleDouble is here for visualizations that take an angle.  For example: BarChart's label angle
  implicit val angleDouble = Angle.converterGraph[Double, JungDirectedGraph](
    Field[Double],
    Eq[Double],
    DirectedGraph[JungDirectedGraph],
    axle.algebra.modules.doubleDoubleModule,
    axle.algebra.modules.doubleRationalModule)

}