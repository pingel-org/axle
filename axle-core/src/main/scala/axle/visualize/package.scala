package axle

import edu.uci.ics.jung.graph.DirectedSparseGraph // for the angleDouble converter

import cats.implicits._

import spire.algebra._

import axle.algebra.DirectedGraph
import axle.quanta.Angle
import axle.quanta.UnitOfMeasurement
import axle.visualize.Color._
import axle.jung.directedGraphJung

package object visualize {

  val defaultColors = List(blue, red, green, orange, pink, yellow)

  // angleDouble is here for visualizations that take an angle.  For example: BarChart's label angle
  implicit val angleDouble = Angle.converterGraphK2[Double, DirectedSparseGraph](
    spire.implicits.DoubleAlgebra, // Field[Double]
    Eq[Double],
    axle.algebra.modules.doubleRationalModule,
    DirectedGraph[DirectedSparseGraph[UnitOfMeasurement[Angle], Double => Double], UnitOfMeasurement[Angle], Double => Double])

}
