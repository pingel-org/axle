package axle

import axle.quanta.Angle

import spire.algebra._
import axle.algebra.DirectedGraph
import axle.jung.JungDirectedGraph
import spire.implicits.DoubleAlgebra

package object visualize {

  // angleDouble is here for visualizations that take an angle.  For example: BarChart's label angle
  implicit val angleDouble = Angle.converterGraph[Double, JungDirectedGraph](
    Field[Double],
    Eq[Double],
    DirectedGraph[JungDirectedGraph],
    axle.algebra.modules.doubleDoubleModule,
    axle.algebra.modules.doubleRationalModule)

}