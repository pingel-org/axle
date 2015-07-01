package axle

import axle.quanta.Angle

import spire.algebra._
import axle.algebra.DirectedGraph
import axle.jung.JungDirectedGraph
import spire.implicits.DoubleAlgebra

import java.awt.Color.blue
import java.awt.Color.green
import java.awt.Color.orange
import java.awt.Color.pink
import java.awt.Color.red
import java.awt.Color.yellow

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