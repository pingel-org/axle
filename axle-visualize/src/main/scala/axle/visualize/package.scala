package axle

import axle.algebra.DirectedGraph
import axle.quanta.Angle
import axle.quanta.UnitOfMeasurement
import axle.visualize.Color._
import cats.kernel.Eq
import spire.algebra.Field
import spire.implicits.DoubleAlgebra
import edu.uci.ics.jung.graph.DirectedSparseGraph
import axle.jung.directedGraphJung
import cats.implicits._

package object visualize {

  val defaultColors = List(blue, red, green, orange, pink, yellow)

  // angleDouble is here for visualizations that take an angle.  For example: BarChart's label angle
  implicit val angleDouble = Angle.converterGraphK2[Double, DirectedSparseGraph](
    Field[Double],
    Eq[Double],
    axle.algebra.modules.doubleDoubleModule,
    axle.algebra.modules.doubleRationalModule,
    DirectedGraph[DirectedSparseGraph[UnitOfMeasurement[Angle], Double => Double], UnitOfMeasurement[Angle], Double => Double])

}
